package com.yww.nexus.config.mybatis;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * <p>
 *      自定义ID生成器
 * </P>
 *
 * @author yww
 * @since 2023/12/9
 */
@Component
public class IdGenerator implements IdentifierGenerator {

    @PostConstruct
    public void idGeneratorRegister() {
        // 创建 IdGeneratorOptions 对象，可在构造函数中输入 WorkerId：
        IdGeneratorOptions options = new IdGeneratorOptions();
        // 机器码位长,默认值6，限定 WorkerId 最大值为2^6-1，即默认最多支持64个节点。
        options.WorkerIdBitLength = 10;
        // 默认值6，限制每毫秒生成的ID个数。若生成速度超过5万个/秒，建议加大 SeqBitLength 到 10。
        options.SeqBitLength = 6;

        // 保存参数（务必调用，否则参数设置不生效）：
        YitIdHelper.setIdGenerator(options);
    }
    
    @Override
    public Number nextId(Object entity) {
        return YitIdHelper.nextId();
    }

    public static void main(String[] args) {
        // 创建 IdGeneratorOptions 对象，可在构造函数中输入 WorkerId：
        IdGeneratorOptions options = new IdGeneratorOptions();
        // 机器码位长,默认值6，限定 WorkerId 最大值为2^6-1，即默认最多支持64个节点。
        options.WorkerIdBitLength = 10;
        // 默认值6，限制每毫秒生成的ID个数。若生成速度超过5万个/秒，建议加大 SeqBitLength 到 10。
        options.SeqBitLength = 6;

        // 保存参数（务必调用，否则参数设置不生效）：
        YitIdHelper.setIdGenerator(options);
        System.out.println(YitIdHelper.nextId());
    }

}
