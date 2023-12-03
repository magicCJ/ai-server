package generator.mapper;

import generator.domain.ConversationTitle;

/**
* @author 13599
* @description 针对表【conversation_title(会话标题历史信息)】的数据库操作Mapper
* @createDate 2023-05-20 21:52:19
* @Entity generator.domain.ConversationTitle
*/
public interface ConversationTitleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(ConversationTitle record);

    int insertSelective(ConversationTitle record);

    ConversationTitle selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ConversationTitle record);

    int updateByPrimaryKey(ConversationTitle record);

}
