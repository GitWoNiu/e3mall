package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.TreeNode;
import cn.e3mall.content.service.ContentCatService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;

@Service
public class ContentCatServiceImpl implements ContentCatService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;

	@Override
	public List<TreeNode> getContentCatList(long parentId) {
		// 1.创建查询条件
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		// 2.执行查询，根据parentId查询内容分类
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		// 3.把内容分类列表转换成TreeNode列表
		List<TreeNode> resultList = new ArrayList<>();
		for (TbContentCategory tbContentCategory : list) {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(tbContentCategory.getId());
			treeNode.setText(tbContentCategory.getName());
			treeNode.setState(tbContentCategory.getIsParent() ? "closed" : "open");
			// 添加到结果列表
			resultList.add(treeNode);
		}
		// 4.返回结果
		return resultList;
	}

	@Override
	public E3Result addContentCategory(long parentId, String name) {
		// 1.创建一个TbContentCategory对象
		TbContentCategory contentCategory = new TbContentCategory();
		// 2.补全属性
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		// （1正常）（2删除）
		contentCategory.setStatus(1);
		// 默认排序值是1
		contentCategory.setSortOrder(1);
		// （1true）（2false）
		contentCategory.setIsParent(false);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		// 3.插入到表中
		contentCategoryMapper.insert(contentCategory);
		// 4.修改父节点的isparent状态
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
		if (!parent.getIsParent()) {
			parent.setIsParent(true);
			// 更新到数据库
			contentCategoryMapper.updateByPrimaryKey(parent);
		}
		// 5.返回E3Result，其中包含TbContentCategory对象
		return E3Result.ok(contentCategory);
	}

	@Override
	public E3Result updateContentCategory(long id, String name) {
		// 1.获取TbContentCategory对象
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		// 2.修改属性
		contentCategory.setName(name);
		// 3.保存修改到数据库
		contentCategoryMapper.updateByPrimaryKey(contentCategory);
		// 4.返回E3Result，其中包含TbContentCategory对象
		return E3Result.ok(contentCategory);
	}

	@Override
	public boolean deleteContentCategory(long id) {
		// 1.获取TbContentCategory对象
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		// 2.判断是否是父节点，能否删除
		if (!contentCategory.getIsParent()) {	//不是父节点
			// 3.获取其父节点，判断修改父节点状态
			//排除父节点ID=0的项，即根节点
			Long parentId = contentCategory.getParentId();
			if (parentId != 0) {
				TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
				//判断父节点是否包含至少两个子节点（执行完删除后，是否仍是父节点）
				// -1.创建查询条件
				TbContentCategoryExample example = new TbContentCategoryExample();
				Criteria criteria = example.createCriteria();
				criteria.andParentIdEqualTo(parentId);
				// -2.执行查询，根据parentId查询内容分类
				List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
				// -3.如果list.size()==1，父节点状态改为isParent=false，否则不需要改变
				if (list!=null && list.size()==1) {
					parent.setIsParent(false);
					// 更新到数据库
					contentCategoryMapper.updateByPrimaryKey(parent);
				}
			}
			//4.执行删除
			contentCategoryMapper.deleteByPrimaryKey(id);
			return true;
		}
		return false;
	}

}
