package generator.domain;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 会话标题历史信息
 * @TableName conversation_title
 */
@Data
public class ConversationTitle implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 商品类型，1-文字会员，2-画图会员，3-GPT4.0会员
     */
    private Integer shopType;

    /**
     * 批次号
     */
    private String batchNo;

    /**
     * 历史标题
     */
    private String historyTitle;

    /**
     * 扩展信息
     */
    private Object feature;

    private static final long serialVersionUID = 1L;
}