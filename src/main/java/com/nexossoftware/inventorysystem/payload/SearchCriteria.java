package com.nexossoftware.inventorysystem.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteria {
    private String name;
    private String userEntered;
    private Date dateEntered;
    public boolean isEmpty() {
        return name == null && userEntered == null && dateEntered == null;
    }
}
