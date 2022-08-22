package org.effectiveengineer.functionaltesting.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder(toBuilder = true)
public class Address{
        String country;
        String region;
        String postalCode;
        String street;
        String street2;
        String unit;
}
