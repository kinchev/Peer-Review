package com.telerik.peer.services;

import com.telerik.peer.exceptions.UnauthorizedOperationException;
import com.telerik.peer.models.Attachment;
import com.telerik.peer.models.User;
import com.telerik.peer.models.WorkItem;
import com.telerik.peer.repositories.contracts.AttachmentRepository;
import com.telerik.peer.services.contracts.AttachmentService;
import com.telerik.peer.utils.FileUploadHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.telerik.peer.services.WorkItemServiceImpl.ONLY_OWNER_AUTHORIZED;

@Service
public class AttachmentServiceImpl implements AttachmentService {
    AttachmentRepository attachmentRepository;

    @Autowired
    public AttachmentServiceImpl(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }


    @Override
    public void delete(long id, User owner) {
    }

    @Override
    public void create(User updatingUser, WorkItem workItem, MultipartFile multipartFile) throws IOException {
        if (workItem.getCreator().getId() != updatingUser.getId()) {
            throw new UnauthorizedOperationException(ONLY_OWNER_AUTHORIZED);
        }
        Attachment attachment = new Attachment();
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            attachment.setWorkItem(workItem);
            attachment.setFileName(fileName);
            attachmentRepository.create(attachment);
            String uploadDir = "src/main/resources/attachments/" + attachment.getId();
            FileUploadHelper.saveFile(uploadDir, fileName, multipartFile);
        }
    }

}

