package com.taskschedular.response;

import com.taskschedular.entity.ResponseStatus;
import com.taskschedular.entity.Status;
import com.taskschedular.entity.Task;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskResponse {

    private ResponseStatus status;
    private List<Task> data;

}
