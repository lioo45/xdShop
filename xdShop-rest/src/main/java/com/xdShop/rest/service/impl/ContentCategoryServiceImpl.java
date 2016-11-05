package com.xdShop.rest.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.xdShop.common.pojo.EUTreeNode;
import com.xdShop.mapper.TbContentCategoryMapper;
import com.xdShop.pojo.TbContentCategory;
import com.xdShop.pojo.TbContentCategoryExample;
import com.xdShop.pojo.TbContentCategoryExample.Criteria;
import com.xdShop.rest.service.ContentCategoryService;

public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper mapper;
	@Override
	public List<EUTreeNode> getContCategoryListByParentId(long parentId) {
		TbContentCategoryExample example=new TbContentCategoryExample();
		Criteria c=example.createCriteria();
		c.andIdEqualTo(parentId);
		List<TbContentCategory> list=mapper.selectByExample(example);
		List<EUTreeNode> trees=new ArrayList<EUTreeNode>();
		for(TbContentCategory tcc:list)
			trees.add(new EUTreeNode((long)tcc.getId(),tcc.getName(),tcc.getIsParent()?"closed":"open"));
		if(trees!=null&&trees.size()>0)
			return trees;
		return null;
	}

}
