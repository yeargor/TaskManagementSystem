package org.example.controller;

import org.example.DTO.CommentDTO;
import org.example.model.User;
import org.example.model.task.Task;
import org.example.model.TaskComment;
import org.example.service.TaskCommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskCommentController.class)
class TaskCommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskCommentService taskCommentService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void createComment_ShouldReturnCreatedComment() throws Exception {
        int taskId = 1;
        String content = "Test comment";
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setContent(content);

        Task task = new Task();
        task.setId(taskId);

        User user = new User();
        user.setId("user1");

        TaskComment createdComment = new TaskComment();
        createdComment.setId(1);
        createdComment.setContent(content);
        createdComment.setTask(task);
        createdComment.setAuthor(user);

        Mockito.when(taskCommentService.createTaskComment(eq(taskId), eq(content)))
                .thenReturn(createdComment);

        mockMvc.perform(post("/tasks/{taskId}/comments", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\": \"" + content + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdComment.getId()))
                .andExpect(jsonPath("$.content").value(content));
    }

    @Test
    void deleteComment_ShouldReturnSuccessMessage() throws Exception {
        int commentId = 1;

        Mockito.doNothing().when(taskCommentService).deleteTaskCommentById(commentId);

        mockMvc.perform(delete("/tasks/{taskId}/comments/{commentId}", 1, commentId))
                .andExpect(status().isOk())
                .andExpect(content().string("Комментарий удалён"));
    }

    @Test
    void getAllComments_ShouldReturnCommentsPage() throws Exception {
        int taskId = 1;
        int page = 0;
        int size = 10;

        mockMvc.perform(get("/tasks/{taskId}/comments/all", taskId)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk());
    }

    @Test
    void getComment_ShouldReturnComment() throws Exception {
        int commentId = 1;
        String content = "Test comment";

        TaskComment comment = new TaskComment();
        comment.setId(commentId);
        comment.setContent(content);

        Mockito.when(taskCommentService.getTaskCommentById(commentId))
                .thenReturn(comment);

        mockMvc.perform(get("/tasks/{taskId}/comments/{commentId}", 1, commentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentId))
                .andExpect(jsonPath("$.content").value(content));
    }
}
