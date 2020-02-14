## 后端API说明文档

### 请求地址

http: //**ip或域名**: 8888/api/**方法名称**

	IP地址尚未确定

### API使用说明

* 方法名称: **login**
	* 方法功能: **玩家登录游戏的时候访问的API，将会更新玩家的昵称(如果微信名称被修改了)，新用户会创建数据**
	* 方法类型: **POST**
	* 请求正文: **带有wxID(String)，nickName(String)的封装类JSON字符串变量accountObj。**例如：accountObj={"wxID":"123456789","nickName":"Steve"}
	* 返回值类型: 返回操作状态字符串
	* 返回值:
		* 登录成功且为老用户返回**OLD\_ACCOUNT**
	    * 登录成功且为新用户**NEW\_ACCOUNT**
	    * 传入参数错误返回**PARAM\_FAULT**
	    * 传入JSON格式错误返回**JSON\_CONVERT\_FAULT**
	    * 其他错误返回**UNKNOWN\_FAULT**

* 方法名称: **setRank**
	* 方法功能: **上传某个玩家的分数**
	* 方法类型: **POST**
	* 请求正文: **玩家的wxID(String)，分数(int)。**注意这是两个参数，例如：wxID=123456789 和 marks=666
	* 返回值类型: **返回操作状态字符串**
	* 返回值:
		* 修改破纪录并修改成功，返回**MODIFIED\_SUCCESS**
		* 分数没有刷新记录，返回**DOESNT\_REFRESH\_RECORD**
		* 未找到此id的用户，返回**CANNOT\_FIND\_USER**
		* 未知错误，返回**UNKNOWN\_FAULT**
  
* 方法名称: **getRank**
	* 方法功能: **获得某个玩家的分数及排名**
	* 方法类型: **POST**
	* 请求正文: **玩家的wxID(String)。**例如：wxID=123456789
	* 返回值类型: **返回JSON字符串或错误信息**
	* 返回值:
		* 操作成功返回**带有分数marks(int)和排名rank(int)的JSON字符串。**例如：{"marks":666, "rank":1}
		* 未根据id找到数据返回**NULL\_WX\_ID**
		* 排名查询错误返回**RANK\_SEARCH\_MISTAKE**
		* 未知错误返回**UNKNOWN\_MISTAKE**

* 方法名称: **getRankTop5**
	* 方法功能: **得到世界前5名的数据**
	* 方法类型: **POST**
	* 请求正文: **无**
	* 返回值类型: **返回JSON字符串或错误信息**
	* 返回值:
		* 操作成功返回**含有1至5个User对象属性的JSON字符串，属性名称分别为top1、top2、top3、top4、top5，其中User是一个含有wxID(String)、nickName(String)和maxMarks(int)的类。**例如：{"top1":{"maxMarks":666,"nickName":"Steve","wxID":"123456789"},"top2":{"maxMarks":123,"nickName":"Ben","wxID":"987654321"}}
		* 未知错误返回**UNKNOWN\_MISTAKE**



