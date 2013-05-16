package com.example.trendchart;

import java.io.Serializable;

public class AllInf implements Serializable {
	
	//��gpa
	private String gpa;
	//��ѧ�ڼ�Ȩ
	private float[] score ;
	//������Ϣ
	private ScoreInf[] sInf ;
	//ѧ��
	private String[] term = new String[3];
	//��ѧ��
	private float totalScore;
	
	public AllInf( String _gpa, float[] _score, ScoreInf[] _sInf, float _totalScore, String[] _term){
		this.gpa = _gpa;
		this.score = _score;
		this.sInf = _sInf;
		this.totalScore = _totalScore;
		this.term = _term;
	}
	
	public String getGPA(){
		return gpa;
	}
	
	public float[] getScore(){
		return score;
	}
	
	public ScoreInf[] getScoreInf(){
		return sInf;
	}
	
	public String[] getTerm(){
		return term;
	}
	
	public float getTotalScore(){
		return totalScore;
	}
	
}
