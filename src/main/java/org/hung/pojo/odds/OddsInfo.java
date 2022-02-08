package org.hung.pojo.odds;

import java.util.Date;

import org.hung.pojo.odds.FullOdds.CombinationOdds;

import lombok.Data;

@Data
public class OddsInfo {

	private FullOdds fullodds;
	private TopNOdds topN;
	private BankerTopNOdds bKTopN;

	public static OddsInfo genf1xf2DblOdds(int f1,int f2) {
		
		//Random rand = new Random();
		//IntStream intStream = rand.ints(1,999);
		//intStream.iterator().ne
		
		OddsInfo oddsInfo = new OddsInfo();
		FullOdds fullOdds = new FullOdds();
		
		CombinationOdds[] odds = new CombinationOdds[f1*f2];
		int n=0;
		int noOfHf = 0;
		int noOfODrp = 0;
		for (int i=0;i<f1;i++) {
			for (int j=0;j<f2;j++) {
				long randOdds = Math.round(Math.random()*999);
				odds[n] = new CombinationOdds();
				odds[n].setCmbStr(String.format("%02d/%02d", i+1, j+1));
				odds[n].setScrOrd(n+1);
				odds[n].setCmbSt("Defined");
				//odds[n].setWP(99999.9);
				odds[n].setOdds(String.valueOf(randOdds));
				if (noOfHf <= 0) {
					odds[n].setHf(true);
					noOfHf++;
				}
				if (noOfODrp <= 10) {
					odds[n].setODrp(30);
					noOfODrp++;
				}
				n++;
			}
		}
		fullOdds.setCmb(odds);
		fullOdds.setColSt("Final");
		fullOdds.setUpdAt(new Date());
		
		oddsInfo.setFullodds(fullOdds);
		
		return oddsInfo;
	}
}
