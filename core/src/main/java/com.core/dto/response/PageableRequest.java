package com.core.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageableRequest {

    int page = 0;
    int size = 10;
    String sortBy = "id";
    Sort.Direction sortType = Sort.Direction.DESC;

    @JsonIgnore
    public PageRequest getPageableRequest() {
        return PageRequest.of(
                page,
                size,
                Sort.by(sortType, sortBy)
        );
    }

}
