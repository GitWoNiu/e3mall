package cn.e3mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.TreeNode;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.pojo.TbItemCatExample.Criteria;
import cn.e3mall.service.ItemCatService;

@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	@Override
	public List<TreeNode> getItemCatList(long parentId) {
		//1.根据parentId查询子节点列表
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		//2.把列表转换成List<TreeNode>
		List<TreeNode> treeNodes = new ArrayList<>();
		for (TbItemCat itemCat : list) {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(itemCat.getId());
			treeNode.setText(itemCat.getName());
			//父节点：close；叶子节点：open
			treeNode.setState(itemCat.getIsParent()?"closed":"open");
			//添加到节点列表
			treeNodes.add(treeNode);
		}
		//3.返回结果
		return treeNodes;
	}

}
