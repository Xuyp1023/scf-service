package com.betterjr.modules.approval;

import com.betterjr.modules.approval.supply.Application;
import com.betterjr.modules.approval.supply.ConfirmLoan;
import com.betterjr.modules.approval.supply.ConfirmScheme;
import com.betterjr.modules.approval.supply.ConfirmTradingBackgrand;
import com.betterjr.modules.approval.supply.EndFlow;
import com.betterjr.modules.approval.supply.OfferScheme;
import com.betterjr.modules.approval.supply.RequestTradingBackgrand;

public class NodeFactory {
	public static BaseNode getSupplyNode(int type) {
		switch (type) {
		case 1:
			return new Application();
		case 2:
			return new OfferScheme();
		case 3:
			return new ConfirmScheme();
		case 4:
			return new RequestTradingBackgrand();
		case 5:
			return new ConfirmTradingBackgrand();
		case 6:
			return new ConfirmLoan();
		case 7:
			return new EndFlow();
		default:
			break;
		}

		return null;
	}
	
	public static BaseNode getSellerNode(int type) {
		switch (type) {
		case 1:
			return new com.betterjr.modules.approval.seller.Application();
		case 2:
			return new com.betterjr.modules.approval.seller.OfferScheme();
		case 3:
			return new com.betterjr.modules.approval.seller.ConfirmScheme();
		case 4:
			return new com.betterjr.modules.approval.seller.RequestTradingBackgrand();
		case 5:
			return new com.betterjr.modules.approval.seller.ConfirmTradingBackgrand();
		case 6:
			return new com.betterjr.modules.approval.seller.ConfirmLoan();
		case 7:
			return new com.betterjr.modules.approval.seller.EndFlow();
		default:
			break;
		}

		return null;
	}
}
