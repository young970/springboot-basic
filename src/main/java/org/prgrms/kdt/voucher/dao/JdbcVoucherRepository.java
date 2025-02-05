package org.prgrms.kdt.voucher.dao;

import org.prgrms.kdt.global.exception.NotUpdateException;
import org.prgrms.kdt.voucher.domain.DiscountPolicy;
import org.prgrms.kdt.voucher.domain.Voucher;
import org.prgrms.kdt.voucher.domain.VoucherType;
import org.prgrms.kdt.voucher.service.dto.SearchRequest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public class JdbcVoucherRepository implements VoucherRepository {
    private final RowMapper<Voucher> voucherRowMapper = (resultSet, i) -> {
        UUID voucherId = UUID.fromString(resultSet.getString("id"));
        VoucherType voucherType = VoucherType.getTypeByStr(resultSet.getString("type"));
        DiscountPolicy discountPolicy = voucherType.createPolicy(resultSet.getDouble("amount"));
        LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        return new Voucher(voucherId, voucherType, discountPolicy, createdAt);
    };

    private final JdbcTemplate jdbcTemplate;

    public JdbcVoucherRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Voucher> findById(UUID voucherId) {
        String sql = "select id, type, amount, created_at from voucher WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql,
                    voucherRowMapper,
                    voucherId.toString()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Voucher insert(Voucher voucher) {
        String sql = "INSERT INTO voucher(id, type, amount, created_at) VALUES (?, ?, ?, ?)";
        int update = jdbcTemplate.update(sql,
                voucher.getVoucherId().toString(),
                voucher.getVoucherType().getDescripton(),
                voucher.getDiscountPolicy().getAmount(),
                Timestamp.valueOf(voucher.getCreatedAt()));
        if (update != 1) {
            throw new NotUpdateException("insert가 제대로 이루어지지 않았습니다.");
        }
        return voucher;
    }

    @Override
    public List<Voucher> findAll(SearchRequest searchRequest) {
        StringBuilder query = new StringBuilder("SELECT id, type, amount, created_at FROM voucher WHERE 1 = 1");
        ArrayList<Object> queryArgs = new ArrayList<>();

        VoucherType voucherType = searchRequest.getVoucherType();
        if (voucherType != null){
            query.append(" AND type = ?");
            queryArgs.add(String.valueOf(voucherType.getDescripton()));
        }

        query.append(" LIMIT ?, ?");
        queryArgs.add(searchRequest.getOffset());
        queryArgs.add(searchRequest.getRecordSize());
        return jdbcTemplate.query(query.toString(), voucherRowMapper, queryArgs.toArray());
    }

    @Override
    public void deleteById(UUID id) {
        String sql = "delete from voucher where id = ?";
        int update = jdbcTemplate.update(sql, id.toString());
        if (update != 1) {
            throw new NotUpdateException("delete가 제대로 이루어지지 않았습니다.");
        }
    }

    @Override
    public List<Voucher> findByType(VoucherType type) {
        String sql = "SELECT id, type, amount, created_at FROM voucher WHERE type = ? ";
        return jdbcTemplate.query(sql, voucherRowMapper, type.getDescripton());
    }
}
