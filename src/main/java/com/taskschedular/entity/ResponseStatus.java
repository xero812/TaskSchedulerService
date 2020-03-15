package com.taskschedular.entity;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseStatus {

    private Status status;
    private int count;

}
