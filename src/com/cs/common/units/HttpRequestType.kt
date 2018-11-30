package com.cs.common.units


enum class HttpRequestType(val value: Int) {
	LOGOUT(99),
	LOGIN(98),
	REGISTER(97),
	DELETE(4),
	PRINT(5),
	AUDIT(6),
	EXPORT(7),
	IMPORT(8),
	RESUME(9),
	PAUSE(10),
	MODIFY(3),
	LIST(1),
	LIST_DETAIL(1),
	ADD(2),
	LOAD(1),
	SELECT(0),
	GETSELF(0),
	DETAIL(1),
	ADD_DETAIL(11),
	DEL_DETAIL(12),
	MODIFY_DETAIL(13),
	UPDATE(41),
	UPDATE_PRICE(43),
	CANCEL(89),
	RESET(101),
	/**
	 * 分配
	 */
	ASSIGNATION(14),
	/**
	 * 回收
	 */
	RETRIEVE(15),
	/**
	 *确认收款
	 */
	RECEIPT(16),
	/**
	 * 发货
	 */
	SHIP(17),

	/**
	 * 断网
	 */
	DOWNWORK(20),

	/**
	 * 复网
	 */
	RENETWORK(21),

	/**
	 * 停机
	 */
	DOWNTIME(22),

	/**
	 * 复机
	 */
	RECUPERATE(23),

	/**
	 * 条形图
	 */
	BARGRAPH(31),

	/**
	 * 折线图
	 */
	FOLDLINEDIAGRAM(32),

	/**
	 *饼图
	 */
	PIECHART(33),

	/**
	 * 曲线图
	 */
	DIAGRAM(34),

	RECHARGE(35),
	ADDPACKAGE(36),
	QUERY(0),
	REPAIR(37),


	//支付
	PAYMENT(38),

	//检测
	TESTING(39),


	//订单撤回
	WITHDRAW(40),

	//发送短信
	SEND(41)


}