package com.xdShop.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xdShop.common.pojo.EUTreeNode;
import com.xdShop.common.pojo.GoodsResult;
import com.xdShop.mapper.TbContentCategoryMapper;
import com.xdShop.pojo.TbContentCategory;
import com.xdShop.pojo.TbContentCategoryExample;
import com.xdShop.pojo.TbContentCategoryExample.Criteria;
import com.xdShop.service.ContentCategoryService;
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper mapper;
	@Override
	public List<EUTreeNode> getContCategoryListByParentId(long parentId) {
		TbContentCategoryExample example=new TbContentCategoryExample();
		Criteria c=example.createCriteria();
		c.andParentIdEqualTo(parentId);
		List<TbContentCategory> list=mapper.selectByExample(example);
		List<EUTreeNode> trees=new ArrayList<EUTreeNode>();
		for(TbContentCategory tcc:list)
			trees.add(new EUTreeNode((long)tcc.getId(),tcc.getName(),tcc.getIsParent()?"closed":"open"));
		if(trees!=null&&trees.size()>0)
			return trees;
		return null;
	}
	@Override
	public GoodsResult insert(long parentId, String name) {
		//插入新节点
		TbContentCategory tcc=new TbContentCategory();
		tcc.setParentId(parentId);
		tcc.setName(name);
		tcc.setIsParent(false);
		tcc.setSortOrder(1);
		tcc.setStatus(1);
		Date date=new Date();
		tcc.setCreated(date);
		tcc.setUpdated(date);
		mapper.insertSelective(tcc);
		//更新父节点状态
		TbContentCategory p=mapper.selectByPrimaryKey(parentId);
		if(p.getIsParent()==false){
			p.setIsParent(true);
			mapper.updateByPrimaryKey(p);
		}
		return GoodsResult.ok(tcc);
	}
	@Override
	public GoodsResult deleteById(long id,long parentId) {
		mapper.deleteByPrimaryKey(id);
		TbContentCategoryExample example=new TbContentCategoryExample();
		Criteria c=example.createCriteria();
		c.andParentIdEqualTo(parentId);
		
		List list=mapper.selectByExample(example);
		if(list==null||list.size()<=0){
			TbContentCategory tcc=new TbContentCategory();
			tcc.setId(parentId);
			tcc.setIsParent(false);
			mapper.updateByPrimaryKeySelective(tcc);
		}
		System.out.println("调用了seriveice");
		return null;
	}
	

}
