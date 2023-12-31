package com.fairgoods.webshop.service;

import com.fairgoods.webshop.model.File;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileService {

    File upload(MultipartFile file);

    Resource get(String reference);
    boolean delete(String reference);

    boolean update(String reference, MultipartFile file);
}