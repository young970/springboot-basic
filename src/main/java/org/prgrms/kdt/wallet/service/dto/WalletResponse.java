package org.prgrms.kdt.wallet.service.dto;

import org.prgrms.kdt.wallet.domain.Wallet;

import java.util.UUID;

public record WalletResponse(UUID walletId, UUID memberId, UUID voucherId) {
    public WalletResponse(Wallet wallet) {
        this(wallet.getWalletId(), wallet.getMemberId(), wallet.getVoucherId());
    }
}
