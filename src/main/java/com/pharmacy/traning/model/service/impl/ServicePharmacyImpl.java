package com.pharmacy.traning.model.service.impl;

import com.pharmacy.traning.exception.DaoException;
import com.pharmacy.traning.exception.ServiceException;
import com.pharmacy.traning.model.dao.PharmacyDao;
import com.pharmacy.traning.model.dao.impl.PharmacyDaoImpl;
import com.pharmacy.traning.model.entity.Pharmacy;
import com.pharmacy.traning.model.service.ServicePharmacy;
import com.pharmacy.traning.model.validator.Validator;
import com.pharmacy.traning.model.validator.impl.ValidatorImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * @author Besarab Victor
 * The type Service pharmacy.
 */
public class ServicePharmacyImpl implements ServicePharmacy {

    private static final Logger logger = LogManager.getLogger();
    private static final PharmacyDao pharmacyDao = PharmacyDaoImpl.getInstance();
    private static final Validator validatorPharmacy = ValidatorImpl.getInstance();
    private static ServicePharmacy instance;

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static ServicePharmacy getInstance() {
        if (instance == null){
            instance = new ServicePharmacyImpl();
        }
        return instance;
    }

    @Override
    public boolean createPharmacy(Pharmacy pharmacy, String number) throws ServiceException {
        if (validatorPharmacy.isName(pharmacy.getCity()) && validatorPharmacy.isName(pharmacy.getStreet())
                && validatorPharmacy.isOnlyNumber(number)){
            pharmacy.setNumber(Integer.parseInt(number));
            try {
                return pharmacyDao.createPharmacy(pharmacy);
            } catch (DaoException e) {
                throw new ServiceException("ServiceException in createPharmacy method. " + e);
            }
        }
        logger.error("Incorrect input data!");
        throw new ServiceException("Incorrect input data!");
    }

    @Override
    public boolean deletePharmacy(long id) throws ServiceException {
        try {
            return pharmacyDao.deletePharmacy(id);
        } catch (DaoException e) {
            throw new ServiceException("ServiceException in deletePharmacy method. " + e);
        }
    }

    @Override
    public boolean restorePharmacy(long id) throws ServiceException {
        try {
            return pharmacyDao.restorePharmacy(id);
        } catch (DaoException e) {
            throw new ServiceException("ServiceException in restorePharmacy method. " + e);
        }
    }

    @Override
    public List<Pharmacy> findAllActualPharmacy() throws ServiceException {
        List<Pharmacy> pharmacyList;
        try {
            pharmacyList = pharmacyDao.findAllActualPharmacy();
        } catch (DaoException e) {
            throw new ServiceException("ServiceException in findAllActualPharmacy method. " + e);
        }
        if (pharmacyList.isEmpty()){
            logger.error("Pharmacy for issuing an order isn't added by administrator.");
            throw new ServiceException("Pharmacy for issuing an order isn't added by administrator.");
        }
        return pharmacyList;
    }

    @Override
    public List<Pharmacy> findAllPharmacy() throws ServiceException {
        try {
            return pharmacyDao.findAllPharmacy();
        } catch (DaoException e) {
            throw new ServiceException("ServiceException in findAllPharmacy method. " + e);
        }
    }
}
