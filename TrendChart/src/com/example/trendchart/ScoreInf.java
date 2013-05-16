package com.example.trendchart;

import java.io.Serializable;

public class ScoreInf implements Serializable{

	//ѧ��
	private String term;
	//�γ���
	private String cname;
	//ѧ��
	private String credit;
	//����
	private String score;
	//����״̬
	//0Ϊ����
	//1Ϊ����
	//2Ϊ˫ѧλ
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
