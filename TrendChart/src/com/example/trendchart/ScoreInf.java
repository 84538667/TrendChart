package com.example.trendchart;

import java.io.Serializable;

public class ScoreInf implements Serializable{

	//学期
	private String term;
	//课程名
	private String cname;
	//学分
	private String credit;
	//分数
	private String score;
	//特殊状态
	//0为正常
	//1为重修
	//2为双学位
	private int inf;
	
	ScoreInf( String _term, String _cname, String _credit, String _score, int _inf){
		this.cname = _cname;
		this.term = _term;
		this.score = _score;
		this.credit = _credit;
		this.inf = _inf;
	}
	
	String getCname(){
		return cname;
	}
	
	String getTerm(){
		return term;
	}
	
	String getScore(){
		return score;
	}
	
	String getCredit(){
		return credit;
	}
	
	int getTextInf(){
		return inf;
	}
	
	boolean isTerm(String _term){
		return this.term.equals(_term);
	}
}
