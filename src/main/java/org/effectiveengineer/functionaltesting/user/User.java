package org.effectiveengineer.functionaltesting.user;

import lombok.Builder;
import lombok.Value;
import org.effectiveengineer.functionaltesting.model.Address;

@Value
@Builder(toBuilder = true)
public class User {
    String id;
    String firstName;
    String lastName;
    Address address;
}
