/*
 * AccessProtectedCustomerManagementService.java
 *
 * (c) Copyright iC Consult GmbH, 2021
 * All Rights reserved.
 *
 * iC Consult GmbH
 * 45128 Essen
 * Germany
 *
 */

package com.icconsult.interview.usermanagement.behavior;

import com.icconsult.interview.usermanagement.api.dto.CustomerRequest;
import com.icconsult.interview.usermanagement.api.dto.CustomerResponse;
import com.icconsult.interview.usermanagement.exception.CustomerNotFoundException;
import com.icconsult.interview.usermanagement.persistance.CustomerEntity;
import com.icconsult.interview.usermanagement.persistance.CustomerRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultCustomerManagementService implements CustomerManagementService {

    private static final String CANNOT_FIND_CUSTOMER_WITH_USER_ID_FORMAT = "Cannot find customer with user id : %s.";
    private static final String CANNOT_FIND_CUSTOMER_FOR_UPDATE_FORMAT = "Cannot find customer for update: [%s].";
    private final Logger logger = LoggerFactory.getLogger(DefaultCustomerManagementService.class);

    @Autowired
    CustomerRepository customerRepository;

    @Override
    public CustomerResponse getCustomer(String userId) {
        CustomerEntity customerEntity = customerRepository.findByUserId(userId).orElseThrow(() -> new CustomerNotFoundException(String.format(CANNOT_FIND_CUSTOMER_WITH_USER_ID_FORMAT, userId)));
        logger.info("Successfully retrieved customer : [givenName={}, familyName={}, email={}].", anonymizeString(customerEntity.getGivenName()), anonymizeString(customerEntity.getFamilyName()), anonymizeString(customerEntity.getEmail()));
        return toCustomerResponse(customerEntity);
    }

    @Override
    public CustomerResponse updateCustomer(String userId, String admin, CustomerRequest newCustomerEntry) {
        CustomerEntity customerEntity = customerRepository.findByUserId(userId).orElseThrow(() -> {
            if (logger.isWarnEnabled()) {
                logger.warn("Tried to update customer entry which doesn't exist in DB. Nonexistent customer uuid: [{}].", userId);
            }
            return new CustomerNotFoundException(String.format(CANNOT_FIND_CUSTOMER_FOR_UPDATE_FORMAT, userId));
        });

        customerEntity.setFamilyName(newCustomerEntry.getFamilyName());
        customerEntity.setGivenName(newCustomerEntry.getGivenName());
        customerEntity.setEmail(newCustomerEntry.getEmail());

        try {
            customerRepository.save(customerEntity);
            logger.info("Customer update successful, new values: [givenName={}, familyName={}, email={}].", anonymizeString(customerEntity.getGivenName()), anonymizeString(customerEntity.getFamilyName()), anonymizeString(customerEntity.getEmail()));
            return toCustomerResponse(customerEntity);
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("Error while trying to update customer [{}]", e.getMessage(), e);
            }
            throw e;
        }
    }

    private CustomerResponse toCustomerResponse(CustomerEntity customerEntity) {
        return new CustomerResponse(customerEntity.getUserId(), customerEntity.getGivenName(), customerEntity.getFamilyName(), customerEntity.getEmail());
    }

    private String anonymizeString(String plaintext) {
        return StringUtils.isBlank(plaintext) || plaintext.length() <= 2 ? plaintext : "*".repeat(plaintext.length());
    }
}
