package com.power.service;

import com.power.domain.SecurityData;
import com.power.error.OWormException;
import com.power.error.OWormExceptionType;
import com.power.repository.SecurityRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * General purpose service exposing reusable logic to other services.
 *
 * @author bp
 */
@Service
public class HelperService {

    @Value("${permission.key}")
    private String permissionKey;

    @Value("${max.retrieval.count}")
    private int maxRetrievalCount;

    @Value("${max.action.count}")
    private int maxActionCount;

    private final SecurityRepository securityRepository;

    public HelperService(final SecurityRepository securityRepository) {
        this.securityRepository = securityRepository;
    }

    public void checkPermission(String permissionKeyArg) {
        if (!permissionKey.equals(permissionKeyArg)) {
            throw new OWormException(OWormExceptionType.INSUFFICIENT_RIGHTS, "You cannot do that.");
        }
    }

    public void checkRetrievalLimit() {
        SecurityData data = getSecurityData();

        data.setDailyRetrievalCount(data.getDailyRetrievalCount() + 1);
        securityRepository.save(data);

        if (data.getDailyRetrievalCount() > maxRetrievalCount) {
            throw new OWormException(OWormExceptionType.INSUFFICIENT_RIGHTS, "Daily retrieval limit reached");
        }
    }

    public void checkActionLimit() {
        SecurityData data = getSecurityData();

        data.setDailyActionCount(data.getDailyActionCount() + 1);
        securityRepository.save(data);

        if (data.getDailyActionCount() > maxActionCount) {
            throw new OWormException(OWormExceptionType.INSUFFICIENT_RIGHTS, "Daily action limit reached");
        }
    }

    private SecurityData getSecurityData() {
        List<SecurityData> dataList = securityRepository.findAll();
        // data has never been created. init
        if (dataList.isEmpty()) {
            SecurityData data = SecurityData
                    .builder()
                    .date(LocalDate.now())
                    .dailyRetrievalCount(0)
                    .dailyActionCount(0)
                    .build();

            securityRepository.save(data);

            return data;
        }

        // will only ever be one
        SecurityData data = dataList.get(0);

        // data is relevant for today, return it
        LocalDate now = LocalDate.now();
        if (now.isEqual(data.getDate())) {
            return data;
        }

        // data is not for today. set it to today and reset counters.
        data.setDate(now);
        data.setDailyRetrievalCount(0);
        data.setDailyActionCount(0);

        securityRepository.save(data);

        return data;
    }
}
