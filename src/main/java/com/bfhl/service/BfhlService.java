package com.bfhl.service;

import com.bfhl.dto.BfhlRequest;
import com.bfhl.dto.BfhlResponse;

/**
 * Contract for the core BFHL processing logic.
 *
 * The implementation is responsible for:
 * <ul>
 *   <li>Classifying each element as numeric (even/odd), alphabetical, or special.</li>
 *   <li>Computing the sum of all numeric elements.</li>
 *   <li>Building the reverse-alternating-caps concat string from alphabetical chars.</li>
 *   <li>Populating user identity fields (user_id, email, roll_number).</li>
 * </ul>
 */
public interface BfhlService {

    /**
     * Process the incoming request and return a fully populated response.
     *
     * @param request validated request containing the data array
     * @return a {@link BfhlResponse} with all computed fields set
     */
    BfhlResponse process(BfhlRequest request);
}
