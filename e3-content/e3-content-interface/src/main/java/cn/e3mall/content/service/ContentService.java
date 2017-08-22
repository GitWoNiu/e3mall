package cn.e3mall.content.service;

import cn.e3mall.common.pojo.DataGridResult;
import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.pojo.TbContent;

public interface ContentService {

	DataGridResult getContentList(Long categoryId, int page, int rows);
	E3Result addContent(TbContent content);
	int editContent(TbContent content);
	int deleteContent(Long id);
}
