package com.telerik.peer;

import com.telerik.peer.models.*;

import java.util.Set;

public class Helper {

    private static User createMockUser() {
        var mockUser = new User();
        mockUser.setId(1);
        mockUser.setUsername("MockUserName");
        mockUser.setPassword("pass");
        mockUser.setEmail("mock@user.com");
        mockUser.setNumber("0987878787");
        mockUser.setPhotoName("photo");
        return mockUser;
    }
    private static Team createMockTeam(){
        var mockTeam=new Team();
        mockTeam.setTeamId(1L);
        mockTeam.setTeamName("TeamName");
        mockTeam.setOwner(createMockUser());
        mockTeam.setWorkItems(createMockWorkItem());
        mockTeam.setMembers(createMockUser());
        return mockTeam;
    }
    private static WorkItem createMockWorkItem(){
        var mockWorkItem=new WorkItem();
        mockWorkItem.setId(1L);
        mockWorkItem.setTitle("title");
        mockWorkItem.setDescription("description");
        mockWorkItem.setCreator(createMockUser());
        mockWorkItem.setReviewer(createMockUser());
        mockWorkItem.setTeam(createMockTeam());
        mockWorkItem.setComments(createMockComment());
        mockWorkItem.setAttachments(createMockAttachment());
        return mockWorkItem;

    }
    private static Comment createMockComment(){
        var mockComment = new Comment();
        mockComment.setId(1L);
        mockComment.setAuthor(createMockUser());
        mockComment.setComment("comment");
        mockComment.setWorkItem(createMockWorkItem());
        return mockComment;
    }

private static Attachment createMockAttachment(){
        var mockAttachment = new Attachment();
        mockAttachment.setId(1L);
        mockAttachment.setFileName("file");
        mockAttachment.setWorkItem(createMockWorkItem());
        return mockAttachment;
}







}
