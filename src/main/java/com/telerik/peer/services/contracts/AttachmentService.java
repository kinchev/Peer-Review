package com.telerik.peer.services.contracts;

import com.telerik.peer.models.Attachment;
import com.telerik.peer.models.User;
import com.telerik.peer.models.WorkItem;
import com.telerik.peer.services.contracts.CRUDService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AttachmentService {

    void delete(long id, User owner);

    void create(User updatingUser, WorkItem workItem, MultipartFile multipartFile) throws IOException;
}
