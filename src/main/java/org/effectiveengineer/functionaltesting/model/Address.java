package org.effectiveengineer.functionaltesting.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Address{
        String country;
        String region;
        String postalCode;
        String street;
        String street2;
        String unit;
}
