package com.telerik.peer;

import com.telerik.peer.models.*;

import java.lang.reflect.Member;
import java.util.HashSet;
import java.util.Set;

public class TestHelpers {
    public static User createMockUser2(){
        return createMockUser();
    }
    public static User createMockUser() {
        var mockUser = new User();

        mockUser.setId(1);
        mockUser.setUsername("MockUserName");
        mockUser.setPassword("Passw0rd#");
        mockUser.setEmail("mock@user.com");
        mockUser.setNumber("0987878787");
        mockUser.setPhotoName("photo");
        return mockUser;
    }
//    private static User createMockUser2() {
//        var mockUser2 = new User();
//
//        mockUser2.setId(2);
//        mockUser2.setUsername("MockUserName2");
//        mockUser2.setPassword("pass2");
//        mockUser2.setEmail("mock2@user.com");
//        mockUser2.setNumber("0987872787");
//        mockUser2.setPhotoName("photo2");
//        return mockUser2;
//    }
    public static Team createMockTeam(){
        var mockTeam=new Team();
        mockTeam.setTeamId(1);
        mockTeam.setTeamName("TeamName");
        mockTeam.setOwner(createMockUser());
        mockTeam.setWorkItems(createMockWorkItemSet());
        mockTeam.setMembers(createMockMemberSet());

        return mockTeam;
    }
    public static WorkItem createMockWorkItem(){
        var mockWorkItem=new WorkItem();
        mockWorkItem.setId(1);
        mockWorkItem.setTitle("title");
        mockWorkItem.setDescription("description");
        mockWorkItem.setCreator(createMockUser2());

        User reviewer=createMockUser2();
        reviewer.setId(2);
        mockWorkItem.setReviewer(reviewer);
        mockWorkItem.setComments(Set.of(new Comment()));
        mockWorkItem.setAttachments(Set.of(new Attachment()));

        return mockWorkItem;

    }
    private static Comment createMockComment(){
        var mockComment = new Comment();
        mockComment.setId(1);
        mockComment.setAuthor(createMockUser());
        mockComment.setComment("comment");
        mockComment.setWorkItem(createMockWorkItem());
        return mockComment;
    }

private static Attachment createMockAttachment(){
        var mockAttachment = new Attachment();
        mockAttachment.setId(1);
        mockAttachment.setFileName("file");
        mockAttachment.setWorkItem(createMockWorkItem());
        return mockAttachment;
}


private static Set<WorkItem> createMockWorkItemSet(){
        Set<WorkItem> mockWorkItems = new HashSet<>();
        mockWorkItems.add(createMockWorkItem());
        return mockWorkItems;

}



    private static Set<Attachment> createMockAttachmentSet(){
        Set<Attachment> mockAttachments = new HashSet<>();
        mockAttachments.add(createMockAttachment());
        return mockAttachments;

    }

    private static Set<Comment> createMockCommentSet(){
        Set<Comment> mockComments = new HashSet<>();
        mockComments.add(createMockComment());
        return mockComments;

    }    private static Set<User> createMockMemberSet(){
        Set<User> mockMembers = new HashSet<>();
        mockMembers.add(createMockUser2());
        return mockMembers;

    }
}
