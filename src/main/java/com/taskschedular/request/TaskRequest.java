package com.taskschedular.request;

import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskRequest {

    private String message;
    private Date timestamp;

}
