package com.mypay.customer_service.services;

import com.mypay.customer_service.Exception.AppException;
import com.mypay.customer_service.Exception.ErrorCode;
import com.mypay.customer_service.dtos.request.BankLinkRequest;
import com.mypay.customer_service.entities.Customer;
import com.mypay.customer_service.entities.LinkedBankAccount;
import com.mypay.customer_service.entities.Wallet;
import com.mypay.customer_service.repositories.CustomerRepository;
import com.mypay.customer_service.repositories.LinkedBankAccountRepository;
import com.mypay.customer_service.repositories.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final CustomerRepository customerRepository;
    private final LinkedBankAccountRepository linkedBankAccountRepository;

    public Wallet getWalletInfo(String accountId) {
        Customer customer = customerRepository.findByAccountId(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_PROFILE_NOT_FOUND));

        return walletRepository.findByCustomerId(customer.getId())
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));
    }
    @Transactional
    public String updateWalletStatus(String accountId, String status) {
        Wallet wallet = getWalletInfo(accountId);

        if (wallet.getStatus().equals(status)) {
            throw new AppException(ErrorCode.WALLET_ALREADY_BLOCKED);
        }

        wallet.setStatus(status);
        walletRepository.save(wallet);
        return "Cập nhật trạng thái ví thành công: " + status;
    }
    public List<LinkedBankAccount> getLinkedBanks(String accountId) {
        Wallet wallet = getWalletInfo(accountId);
        return linkedBankAccountRepository.findAllByWalletId(wallet.getId());

    }
    @Transactional
    public String linkBankAccount(String accountId, BankLinkRequest request) {
        Wallet wallet = getWalletInfo(accountId);

        if (linkedBankAccountRepository.existsByWalletIdAndAccountNumber(wallet.getId(), request.getAccountNumber())) {
            throw new AppException(ErrorCode.BANK_ACCOUNT_ALREADY_EXISTS);
        }

        boolean hasPrimary = linkedBankAccountRepository.findByWalletIdAndIsPrimaryTrue(wallet.getId()).isPresent();

        LinkedBankAccount bankAccount = LinkedBankAccount.builder()
                .wallet(wallet)
                .bankCode(request.getBankCode().toUpperCase())
                .accountNumber(request.getAccountNumber())
                .accountName(request.getAccountName())
                .isPrimary(!hasPrimary)
                .linkedAt(LocalDateTime.now())
                .build();

        linkedBankAccountRepository.save(bankAccount);
        return "Liên kết tài khoản ngân hàng thành công!";
    }
    @Transactional
    public String setPrimaryBank(String accountId, String bankId) {
        Wallet wallet = getWalletInfo(accountId);

        LinkedBankAccount targetBank = linkedBankAccountRepository.findByIdAndWalletId(bankId, wallet.getId())
                .orElseThrow(() -> new AppException(ErrorCode.BANK_ACCOUNT_NOT_FOUND));


        linkedBankAccountRepository.findByWalletIdAndIsPrimaryTrue(wallet.getId()).ifPresent(oldPrimary -> {
            oldPrimary.setPrimary(false);
            linkedBankAccountRepository.save(oldPrimary);
        });


        targetBank.setPrimary(true);
        linkedBankAccountRepository.save(targetBank);

        return "Đã đổi tài khoản ngân hàng mặc định thành công!";
    }


    @Transactional
    public String deleteLinkedBank(String accountId, String bankId) {
        Wallet wallet = getWalletInfo(accountId);

        LinkedBankAccount bankAccount = linkedBankAccountRepository.findByIdAndWalletId(bankId, wallet.getId())
                .orElseThrow(() -> new AppException(ErrorCode.BANK_ACCOUNT_NOT_FOUND));

        if (bankAccount.isPrimary()) {
            throw new RuntimeException("Không thể xóa tài khoản ngân hàng mặc định. Vui lòng chọn thẻ khác làm mặc định trước!");
        }

        linkedBankAccountRepository.delete(bankAccount);
        return "Hủy liên kết ngân hàng thành công!";
    }
//---------INTERNAL

    public Wallet getWalletByCustomerId(String customerId) {
        return walletRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));
    }

    public Wallet getWalletByAccountIdInternal(String accountId) {
        Customer customer = customerRepository.findByAccountId(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.CUSTOMER_PROFILE_NOT_FOUND));
        return getWalletByCustomerId(customer.getId());
    }


    @Transactional
    public String deductBalance(String walletId, BigDecimal amount) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new AppException(ErrorCode.INSUFFICIENT_FUNDS);
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);
        return "Trừ tiền thành công!";
    }


    @Transactional
    public String addBalance(String walletId, BigDecimal amount) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
        return "Cộng tiền thành công!";
    }

    @Transactional
    public String freezeBalance(String walletId,BigDecimal amount) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new AppException(ErrorCode.INSUFFICIENT_FUNDS);
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        wallet.setFrozenBalance(wallet.getFrozenBalance().add(amount));
        walletRepository.save(wallet);
        return "Đóng băng số dư thành công!";
    }

}