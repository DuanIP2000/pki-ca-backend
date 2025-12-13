package com.algorithm.pki_ca_backend.controller;

import com.algorithm.pki_ca_backend.dto.ApiResponse;
import com.algorithm.pki_ca_backend.entity.CertificateEntity;
import com.algorithm.pki_ca_backend.entity.CertificateRequestEntity;
import com.algorithm.pki_ca_backend.entity.UserEntity;
import com.algorithm.pki_ca_backend.repository.CertificateRequestRepository;
import com.algorithm.pki_ca_backend.repository.UserRepository;
import com.algorithm.pki_ca_backend.service.CertificateService;
import com.algorithm.pki_ca_backend.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController{

    private final UserRepository userRepository;
    private final CertificateRequestRepository requestRepository;

    // 构造器注入
    public CertificateController(
            UserRepository userRepository,
            CertificateRequestRepository requestRepository
    ) {
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
    }

    @Autowired
    private OperationLogService logService;

    @Autowired
    private CertificateService certificateService;

    // 查询所有证书
    @GetMapping
    public ApiResponse<List<CertificateEntity>> getAllCertificates(){
        List<CertificateEntity> list = certificateService.getAllCertificates();
        return ApiResponse.success(list);
    }

    // 签发新证书（！！！ 签发旧接口，不再使用，但为了系统稳定不进行删除）
    @PostMapping("/issue")
    public ApiResponse<CertificateEntity> issueCertificate(
            @RequestParam Integer userId,
            @RequestBody CertificateEntity cert){

        CertificateEntity saved = certificateService.issueCertificate(userId, cert);

        if (saved == null){
            return ApiResponse.fail("用户不存在或证书序列号已存在");
        }

        return ApiResponse.success(saved);
    }

    // 查询证书状态
    @GetMapping("/{certId}/status")
    public ApiResponse<String> getCertificateStatus(@PathVariable Integer certId){

        CertificateEntity cert = certificateService.getCertificateById(certId);

        if (cert == null){
            return ApiResponse.fail("证书不存在");
        }

        String status = certificateService.evaluateCertificateStatus(cert);
        return ApiResponse.success(status);
    }

    // 根据SerialNumber查询证书
    @GetMapping("/status")
    public ApiResponse<String> getCertificateStatusBySerialNumber(@RequestParam String serialNumber){

        CertificateEntity cert =
                certificateService.getCertificateBySerialNumber(serialNumber);

        if (cert == null){
            return ApiResponse.fail("证书不存在");
        }

        String status = certificateService.evaluateCertificateStatus(cert);
        return ApiResponse.success(status);
    }

    // 根据SerialNumber的证书验证接口
    @GetMapping("/{serialNumber}/verify")
    public ApiResponse<Map<String, Object>> verifyCertificate(
            @PathVariable String serialNumber){

        // 1. 根据序列号查证书
        CertificateEntity cert = certificateService.getCertificateBySerialNumber(serialNumber);

        if(cert == null){
            return ApiResponse.fail("证书不存在");
        }

        // 2. 计算证书状态
        String status = certificateService.evaluateCertificateStatus(cert);

        // 3. 状态不是“有效”则视为不可信
        if (!"有效".equals(status)){
            return ApiResponse.fail("证书不可用，状态：" + status);
        }

        // 4. 构造验证成功返回数据
        Map<String, Object> data = new HashMap<>();
        data.put("status", status);
        data.put("validFrom", cert.getValidFrom());
        data.put("validTo", cert.getValidTo());

        return ApiResponse.success(data);
    }

    @PostMapping("/apply")
    public ApiResponse<Long> applyCertificate(Authentication authentication) {

        String username = authentication.getName();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        CertificateRequestEntity req = new CertificateRequestEntity();
        req.setUser(user);
        req.setPublicKey(user.getPublicKey());
        req.setStatus("PENDING");
        req.setRequestTime(LocalDateTime.now());

        // 持久化保存
        CertificateRequestEntity saved = requestRepository.save(req);

        // 记录日志
        logService.record(
                username,
                "申请证书",
                "CertificateRequest",
                "requestId=" + saved.getRequestId()
        );

        return ApiResponse.success(saved.getRequestId());
    }

    // 查询待签发证书列表
    @GetMapping("/requests")
    public ApiResponse<List<CertificateRequestEntity>> listPendingRequests() {
        return ApiResponse.success(requestRepository.findByStatus("PENDING"));
    }

    // 签发待签发列表中的证书
    @PostMapping("/requests/{id}/approve")
    public ApiResponse<Long> approveRequest(@PathVariable Long id, Authentication authentication){
        // 1. 查找证书申请
        CertificateRequestEntity req = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("证书申请不存在"));

        // 2. 校验状态
        if (!"PENDING".equals(req.getStatus())) {
            return ApiResponse.fail("该申请不是待审批状态");
        }

        // 3. 调用 CA 服务签发证书
        CertificateEntity cert = certificateService.issueCertificateFromRequest(
                req,
                authentication.getName()   // ADMIN 用户名
        );

        // 4. 更新申请状态
        req.setStatus("APPROVED");
        req.setApproveTime(LocalDateTime.now());
        requestRepository.save(req);

        // 5. 返回证书 ID
        return ApiResponse.success(cert.getCertId().longValue());
    }

    @GetMapping("/{certId}/download")
    public ResponseEntity<String> downloadCertificate(
            @PathVariable Integer certId,
            Authentication authentication
    ) {

        CertificateEntity cert =
                certificateService.getCertificateById(certId);

        if (cert == null) {
            return ResponseEntity.notFound().build();
        }

        String username = authentication.getName();
        String role = authentication.getAuthorities()
                .iterator().next().getAuthority();

        // USER 只能下载自己的证书
        if ("ROLE_USER".equals(role)) {
            if (!cert.getUser().getUsername().equals(username)) {
                throw new org.springframework.security.access.AccessDeniedException("无权下载该证书");
            }
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=certificate-" + certId + ".pem")
                .contentType(MediaType.valueOf("application/x-pem-file"))
                .body(cert.getCertPEM());
    }
}
