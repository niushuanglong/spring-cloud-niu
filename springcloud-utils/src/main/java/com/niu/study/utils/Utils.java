package com.niu.study.utils;

import com.thoughtworks.xstream.core.util.Base64Encoder;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RegExUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author   wq
 * @since    Ver 1.1
 * @Date  2013-11-19  下午12:13:19
 * @see  
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class Utils {

	//List中重复元素
	public static <E> List<E> getDuplicateElements(List<E> list) {
		return list.stream()
				.collect(Collectors.toMap(e -> e, e -> 1, Integer::sum)) // 获得元素出现频率的 Map，键为元素，值为元素出现的次数
				.entrySet()
				.stream()                              // 所有 entry 对应的 Stream
				.filter(e -> e.getValue() > 1)         // 过滤出元素出现次数大于 1 (重复元素）的 entry
				.map(Map.Entry::getKey)                // 获得 entry 的键（重复元素）对应的 Stream
				.collect(Collectors.toList());         // 转化为 List
	}
    /**
     * 获取临时文件目录
     * @return
     * @throws IOException 
     */
    public static String getTmpDir() {
        String tmpDir="/tmpdir"+new SimpleDateFormat("yyyyMMdd").format(new Date());
        File d = new File(tmpDir);
        if(!d.exists()){
            boolean success = d.mkdirs();
            if(!success){
                throw new RuntimeException("无法创建目录:"+tmpDir);
            }
        }
        return tmpDir;
    }
	/**
     * 创建一个空文件路径
     * @param baseDir
     * @return
     * @throws IOException 
     */
    public static String createUploadFilePath(String baseDir,String fileExtName) {
        try {
            String relativeFilePath = getNewFilePath(baseDir)+fileExtName;
            File dest = new File(getAbsoluteFilePath(baseDir,relativeFilePath));
            dest.createNewFile();
            
            return relativeFilePath;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 保存文件到指定目录
     * @param baseDir
     * @param f
     * @return
     */
	public static String saveUploadFile(String baseDir,File f) {
		try {
			String relativeFilePath = getNewFilePath(baseDir);
			saveUploadFile(baseDir, relativeFilePath, new FileInputStream(f));
			return relativeFilePath;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
    /**
     * 保存文件到指定目录
     * @param baseDir
     * @param in
     * @return
     */
	public static String saveUploadFile(String baseDir,InputStream in) {
		try {
			String relativeFilePath = getNewFilePath(baseDir);
        	saveUploadFile(baseDir, relativeFilePath, in);
			return relativeFilePath;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	/**
     * 保存文件到指定目录
     * @param baseDir
     * @param in
     * @param fileExtName 文件扩展名
     * @return
     */
    public static String saveUploadFile(String baseDir,InputStream in,String fileExtName) {
        try {
        	String relativeFilePath = getNewFilePath(baseDir)+fileExtName;
        	saveUploadFile(baseDir, relativeFilePath, in);
        	return relativeFilePath;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	/**
     * 保存文件到指定目录
     * @param baseDir
     * @param in
     * @return
     */
    public static void saveUploadFile(String baseDir,String relativeFilePath,InputStream in) {
        OutputStream os =null;
        // 每天一个目录，如果目录不存在，创建它
        try {
            File dest = new File(getAbsoluteFilePath(baseDir,relativeFilePath));
            File parentF=dest.getParentFile();
            if(!parentF.exists()){
                parentF.mkdirs();
            }
            if(!dest.exists()) {
            	dest.createNewFile();
            }
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[5*1024*1024];
            int s = -1;
            while ((s = in.read(buffer)) != -1) {
                os.write(buffer, 0, s);
            }
            os.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally{
            clean(os,in);
        }
    }
	/**
	 * 拷贝文件
	 * @param source
	 * @param dest
	 */
	public final static void copyFile(File source,File dest){
		byte[] buffer = new byte[1024*10];
		InputStream sin = null;
		OutputStream dout = null;
		try{
			sin = new FileInputStream(source);
			dout = new FileOutputStream(dest);
			int len=-1;
			while((len=sin.read(buffer))>-1){
				dout.write(buffer, 0, len);
			}
		}catch(Exception  e){
			throw new RuntimeException(e);
		}finally{
			clean(sin);
			clean(dout);
		}
	}
	/**
	 * 生成并获取当日一个信的文件目录
	 * @param baseDir
	 * @return
	 * @throws IOException
	 */
	public final static String getNewFilePath(String baseDir) throws IOException {
		Date date = new Date();
		String dir = new SimpleDateFormat("yyyy").format(date)+"/"
					+ new SimpleDateFormat("MM").format(date)+"/"
					+ new SimpleDateFormat("dd").format(date);
		
		File file = new File(baseDir+ "/" + dir);
		if (!file.exists()) {
			file.mkdirs();
		}
		// 保存文件，并将文件相对路径保存在relativeFilePaths变量中
		String relativeFilePath = new StringBuilder(dir).append("/")
		        .append(UUID.randomUUID().toString()).toString();
		return relativeFilePath;
	}
	/**
	 * 获取绝对路径
	 * @param baseDir
	 * @param relativePath
	 * @return
	 */
	public static String getAbsoluteFilePath(String baseDir,String relativePath) {
		//防止删除整个目录
		if(StringUtils.isBlank(relativePath))
			throw new RuntimeException("can not retrieve file by NULL path");
		return baseDir + "/" + relativePath;
	}
	/**
	 * 删除文件
	 * @param file
	 */
	public static void deleteFile(File file) {
		if(file==null||!file.exists()) {
			return;
		}
		if(file.isDirectory()) {
			for (File c : file.listFiles()) {
				deleteFile(c);
			}
		}
		file.delete();
	}
	/**
	 * 将文件以字节数组形式读出
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public static byte[] readFile(File f) throws IOException {
		ByteArrayOutputStream bout = null;
		InputStream in = null;
		try {
			bout = new ByteArrayOutputStream();
			in = new FileInputStream(f);
			byte[] buffer = new byte[1024 * 1024];
			int l = -1;
			while ((l = in.read(buffer)) != -1) {
				bout.write(buffer, 0, l);
				bout.flush();
			}
			bout.flush();
			return bout.toByteArray();
		} finally {
			bout.close();
			in.close();
		}
	}
	/**
     * 获取字符流的内容
     * @param tInputStream
     * @return
     */
    public static String getStreamString(InputStream tInputStream){
        if (tInputStream != null){
            try{
                BufferedReader tBufferedReader = new BufferedReader(new InputStreamReader(tInputStream,"UTF-8"));
                StringBuffer tStringBuffer = new StringBuffer();
                String sTempOneLine="";
                while ((sTempOneLine=tBufferedReader.readLine())!= null){
                    tStringBuffer.append(sTempOneLine);
                }
                return tStringBuffer.toString();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return "";
    }
	/**
	 * 将文件以字符串形式读出
	 * @param f
	 * @return
	 * @throws IOException
	 */
	public static String readFileToString(File f) throws IOException{
		return getStreamString(new FileInputStream(f));
	}
	/**
	 * 将字符串写入文件
	 * @param f
	 * @param str
	 * @throws IOException
	 */
	public static void writeStringToFile(File f,String str) throws IOException{
		if(!f.exists()){
			f.createNewFile();
		}
		Writer write = new OutputStreamWriter(new FileOutputStream(f)); 
		write.write(str);
		write.close();
	}
    /**
     * 获取文件下载名称
     * @param req
     * @param fileName 文件名称
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encodeExportFileName(HttpServletRequest req, String fileName){
        String filename="";//IE9之前包括IE9都包含MSIE;IE10之后都包含Trident;edge浏览器包含Edge
        try {
            String userAgent=req.getHeader("User-Agent");
            if (userAgent.contains("MSIE") ||userAgent.contains("Trident")||userAgent.contains("Edge")) { 
                 if(fileName.length()>150){//解决IE 6.0 bug
                     filename=new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
                 }else{
                     filename = toUtf8String(fileName);
                 }
            } else {  
                filename = toUtf8String(fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            filename=fileName;
        }
        return filename;
    }
	
	/**
	 * 将字符串已utf-8编码
	 * @param s
	 * @return
	 */
	public static final  String toUtf8String(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c >= 0 && c <= '\377'){
				sb.append(c);
			}else{
			    appendUTF8AsciiStr(sb, c);
			}
		}
		return sb.toString();
	}
	private static void appendUTF8AsciiStr(StringBuffer sb, char c) {
		byte[] b = null;
		try {
			b = (new Character(c)).toString().getBytes("UTF-8");
		} catch (Exception ex) {
			b = new byte[0];
		}
		for (int j = 0; j < b.length; j++) {
			int k = b[j];
			if (k < 0)
				k += 256;
			sb.append("%" + Integer.toHexString(k).toUpperCase());
		}
	}
	/**
	 * 将字节数转换为kb或mb数
	 */
	private static final BigDecimal M1 = new BigDecimal(1024*1024);
	private static final BigDecimal K1 = new BigDecimal(1024);
	public static final String getHumenReadableSize(long size){
		 int scale = 2;
		BigDecimal bsize =new BigDecimal(size);
		if(bsize.compareTo(M1)<0){
			if(bsize.compareTo(K1)<0){
				return size+"B";
			}
			else{
				return bsize.divide(K1,scale,BigDecimal.ROUND_HALF_UP)+"K";
			}
		}else{
			return bsize.divide(M1,scale,BigDecimal.ROUND_HALF_UP)+"M";
		}
	}
	/**
	 * 将字节数转换为kb或mb数
	 * @param size
	 * @return
	 */
	public static final String getHumenReadableSize2(long size){
		int scale = 2;
		BigDecimal bsize =new BigDecimal(size);
		if(bsize.compareTo(M1)<0){
			if(bsize.compareTo(K1)<0){
				return size+"B";
			}
			else{
				return bsize.divide(K1,scale,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()+"K";
			}
		}else{
			return bsize.divide(M1,scale,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()+"M";
		}
	}
	/**
	 * 转义html特殊字符
	 * @param html
	 * @return
	 */
	public static String escapeHtml(String html) {
		if (html == null) return null;
		return html.replaceAll("<", "&lt;").replaceAll(">", "&gt;")
				.replaceAll("\"", "&quot;");
	}
	/**
	 * 反转义html特殊字符
	 * @param str
	 * @return
	 */
	public static String reEscapeHtml(String str) {
		if (str == null) return null;
		return str.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
				.replaceAll("&quot;", "\"");
	}
   /**
     * 分割集合
     * @param collection
     * @param num
     * @return
     */
    public static List splitToFixedList(Collection collection, int num) {
        List<List<Object>> result = new ArrayList<List<Object>>();
        List<Object> l = new ArrayList<Object>();
        int i = 0;
        for (Object object : collection) {
        	if (i == 0 || i % num != 0)
                l.add(object);
            else {
                result.add(l);
                l = new ArrayList<Object>();
                l.add(object);
            }
            i++;
		}
        if (!l.isEmpty()&&!result.contains(l))
            result.add(l);
        return result;
    }
	/**
	 * 分割字符串
	 * @param ids
	 * @param regex
	 * @return
	 */
    public static List<String> splitString(String ids,String regex ) {
		List<String> idList = new ArrayList<String>();
		if(StringUtils.isNotBlank(ids)){
			String[] idArr = ids.split(regex);
			for(String id:idArr){
				if(StringUtils.isNotBlank(id))
					idList.add(id.trim());
			}
		}
		return idList;
	}
	/**
	 * 分割字符串，如果开始位置大于字符串长度，返回空
	 * @param str
	 * @param f
	 * @param t
	 * @return
	 */
	public static String substring(String str, int f, int t) {
        if (f > str.length())
            return null;
        if (t > str.length()) {
            return str.substring(f, str.length());
        } else {
            return str.substring(f, t);
        }
    }
	/**
     * 链接集合字符串
     * @param collection 集合
     * @param joinSymbol 拼接符号 默认","
     * @return
     */
    public static String concatCollectionString(Collection<String> collection, String joinSymbol) {
        StringBuffer buffer = new StringBuffer();
        if(collection != null){
        	if(joinSymbol==null) {
        		joinSymbol=",";
        	}
        	boolean join=false;
            for(String a:collection ){
            	if(join) {
                	buffer.append(joinSymbol);
                }else {
                	join=true;
                }
                buffer.append(a);
            }
        }
        return buffer.toString();
    }
	/**
	 * 获取类中的静态常量
	 * @param prefixFieldName
	 * @param clazz
	 * @return
	 */
    private static Map<String,List> prefixFieldNameToConstants=new HashMap<String,List>();
	public static List getStaticConstantsFieldValuesByPrefixFieldName(
			String prefixFieldName, Class clazz) {
		String key=clazz.getName()+"_"+prefixFieldName;
		List l = prefixFieldNameToConstants.get(key);
		if(l==null) {
			l = new ArrayList();
			prefixFieldNameToConstants.put(key, l);
			Field[] fs = clazz.getDeclaredFields();// 取得当前类的所以属性
			for (Field f : fs) {// 把FS的值付给f进行循繁
				if (f.getName().startsWith(prefixFieldName)) {
					try {
						Object v = f.get(clazz);
						if (v != null)
							l.add(v);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		return l;
	}
	/**
	 * 将参数封装为map
	 * @param params
	 * @return
	 */
	public static final Map<String, Object> buildMap(Object...params){
		Map<String, Object> m  = new HashMap<String, Object>();
		if(params!=null){
			if(params.length%2!=0){
				throw new RuntimeException("参数必须成对出现，参数名，参数值作为一对!");
			}
			for(int i=0;i<params.length;i+=2){
				String k =(String) params[i];
				Object v = params[i+1];
				if(StringUtils.isNotBlank(k)){
					m.put(k, v);
				}
			}
		}
		return m;
	}
	/**
     * 将参数封装为map
     * @param objects
     * @return
     */
    public static final Map<String,Object> buildMap2(Collection objects){
		if(objects==null){
			return null;
		}
		if(objects.size()%2!=0){
			throw new IllegalArgumentException("数组的个数必须是2的倍数！");
		}
		Map<String,Object> map = new HashMap<String, Object>();
		int i=0;String key=null;
		for (Object obj : objects) {
			if(i%2==0) {
				key=obj+"";
			}else {
				map.put(key, obj);
			}
			i++;
		}
		
		return map;
	}
	/**
	 * 转义like语句中的 <code>'_'</code><code>'%'</code> 将<code>'?'</code>转成sql的
	 * <code>'/_'</code> 将<code>'%'</code>转成sql的<code>'/%'</code>
	 * <p>
	 * 例如搜索<code>?aa*bb?c_d%f</code>将转化成<br/>
	 * <code>_aa%bb_c/_d/%f</code>
	 * </p>
	 * @param likeStr
	 * @return
	 */
	public static String escapeSQLLike(String likeStr) {
		String str = likeStr;
		str = StringUtils.replace(str, "/", "//");
		str = StringUtils.replace(str, "_", "/_"); // sqlserver 中下划线不用转义
		str = StringUtils.replace(str, "%", "/%");
//		str = StringUtils.replace(str, "?", "_");
//		str = StringUtils.replace(str, "*", "%");
		return str;
	}
	
	public static List<String> escapeKeyWordGroups=new ArrayList<String>();
	static{
		escapeKeyWordGroups.add("ABOUT");
		escapeKeyWordGroups.add("ACCUM");
		escapeKeyWordGroups.add("AND");
		escapeKeyWordGroups.add("BT");
		escapeKeyWordGroups.add("BTG");
		escapeKeyWordGroups.add("EQUIV");
		escapeKeyWordGroups.add("FUZZY");
		escapeKeyWordGroups.add("HASPATH");
		escapeKeyWordGroups.add("INPATH");
		escapeKeyWordGroups.add("MDATA");
		escapeKeyWordGroups.add("MINUS");
		escapeKeyWordGroups.add("NEAR");
		escapeKeyWordGroups.add("NOT");
		escapeKeyWordGroups.add("NT");
		escapeKeyWordGroups.add("NTG");
		escapeKeyWordGroups.add("NTI");
		escapeKeyWordGroups.add("NTP");
		escapeKeyWordGroups.add("OR");
		escapeKeyWordGroups.add("PT");
		escapeKeyWordGroups.add("RT");
		escapeKeyWordGroups.add("SQE");
		escapeKeyWordGroups.add("SYN");
		escapeKeyWordGroups.add("TR");
		escapeKeyWordGroups.add("TRSYN");
		escapeKeyWordGroups.add("TT");
		escapeKeyWordGroups.add("WITHIN");
	}
	/**
	 * 转义全文检索的特殊字符
	 * @param likeStr
	 * @return
	 */
	public static String escapeSQLContentKeyWordLike(String likeStr) {
		String str = likeStr;
		str=str.replaceAll("\\>", "\\\\>")
				.replaceAll("\\!", "\\\\!")
				.replaceAll("\\$", "\\\\\\$")
				.replaceAll("\\{","\\\\{")
				.replaceAll("\\}", "\\\\}")
				.replaceAll("\\)", "\\\\)")
				.replaceAll("\\(", "\\\\(")
				.replaceAll("\\*", "\\\\*")
				.replaceAll("\\&", "\\\\&")
				.replaceAll("\\~", "\\\\~")
				.replaceAll("\\-", "\\\\-")
				.replaceAll("\\=", "\\\\=")
				.replaceAll("\\|", "\\\\|")
				.replaceAll("\\,", "\\\\,")
				.replaceAll("'", "''")
				.replaceAll("\\?", "\\\\?")
				.replaceAll("\\;", "\\\\;")
				.replaceAll("\\[", "\\\\[")
				.replaceAll("\\]", "\\\\]")
				.replaceAll("\\%", "\\\\%");
		
		return str;
	}
	//批量转义字符
	public static List<String> escapeSQLContentKeyWords(List<String> keyWords) {
		List<String> keyWords2=new ArrayList<String>();
		if(keyWords!=null&&keyWords.size()>0){
			for(String word:keyWords){
				word=Utils.escapeSQLContentKeyWordLike(word);
				if(escapeKeyWordGroups.contains(word.toUpperCase())){
					word="/"+word;
				}
				keyWords2.add(word);
			}
		}
		return keyWords2;
	}
	/**
	 * 数字相加
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	public static final BigDecimal add(Object obj1, Object obj2){
		return objectToBigDecimal(obj1).add(objectToBigDecimal(obj2));
	}
    /**
     * 数字相减
     * @param obj1
     * @param obj2
     * @return
     */
	public static final BigDecimal subtract(Object obj1, Object obj2){
		return objectToBigDecimal(obj1).subtract(objectToBigDecimal(obj2));
	}
	/**
     * 数字相除 且四舍五入
     * @param obj1
     * @param obj2
     * @param scale
     * @return
     */
	public static final BigDecimal divide(Object obj1, Object obj2,int scale){
		BigDecimal bd1 = objectToBigDecimal(obj1);
		BigDecimal bd2 = objectToBigDecimal(obj2);
		return bd1.divide(bd2,scale,BigDecimal.ROUND_HALF_UP);
	}
	/**
     * 数字相乘
     * @param obj1
     * @param obj2
     * @return
     */
	public static final BigDecimal multiply(Object obj1, Object obj2){
		BigDecimal bd1 = objectToBigDecimal(obj1);
		BigDecimal bd2 = objectToBigDecimal(obj2);
		
		return bd1.multiply(bd2);
	}
	/**
	 * 计算同比或环比
	 * @param obj1 本期
	 * @param obj2 上期或同期
	 * @return
	 */
	public static final String getTbOrHb(Object obj1, Object obj2, int scale){
		if(obj1==null || obj2==null) return "--";
		BigDecimal bd2 = objectToBigDecimal(obj2);
		if(bd2.compareTo(new BigDecimal(0)) == 0){
			return "--";
		}
		if(scale<0){
			scale = 0;
		}
		BigDecimal bd = Utils.subtract(obj1, obj2);
		BigDecimal resultBd = divide(multiply(bd,Integer.valueOf(100)),bd2,20);
		return resultBd.setScale(scale, BigDecimal.ROUND_HALF_UP).toString()+"%";
	}
	/**
	  * 把object转换成BigDecimal，object为空或非数字，都转换成BigDecimal(0)
	  * @param obj
	  * @return
	  */
	public static final BigDecimal objectToBigDecimal(Object obj){
		int scale = 20;
		if(obj!=null && obj instanceof Number){
			return new BigDecimal(obj+"").setScale(scale, BigDecimal.ROUND_HALF_UP);
		}else{
			return new BigDecimal(0).setScale(scale, BigDecimal.ROUND_HALF_UP);
		}
	}
	/**
	 * 以特定字符补齐字符串 在字符串前补
	 * @param str
	 * @param len
	 * @param pad
	 * @return
	 */
	public static final  String lpadStr(String str, int len, char pad) {
		if(str == null){
			str="";
		}
		int strLen = str.length();
		for (int i = 0; i < len - strLen; i++) {
			str = pad + str;
		}
		return str;
	}
	/**
	 * 将数字存入缓冲区
	 * @param val
	 * @return
	 */
	public static final byte[] bytes(int val) {
		ByteBuffer bf = ByteBuffer.allocate(4);
		bf.putInt(val);
		byte[] data = bf.array();
		return data;
	}
	/**
     * 将字节存入缓冲区
     * @param data
     * @return
     */
	public static final int valFromBytes(byte[] data) {
		ByteBuffer bf = ByteBuffer.allocate(4);
		bf.put(data);
		bf.flip();
		return bf.getInt();
	}
	/**
	 * 从map中取出指定key(不区分大小写)的值
	 * @param key
	 * @param r
	 * @return
	 */
	public static Object getValIgnoreCaseStrKey(String key, Map r) {
	    if(StringUtils.isBlank(key)){
	        return null;
	    }
		Object val = r.get(key);
		if (val == null)
			val = r.get(key.toUpperCase());
		if (val == null)
			val = r.get(key.toLowerCase());
		if(val==null){
		    for(Object k:r.keySet()){
		        if(key.equalsIgnoreCase(k.toString())){
		            val = r.get(k);
		            break;
		        }
		    }
		}
		return val;
	}
	/**
	 * 根据阿拉伯数字获取中文数字
	 * @param num
	 * @return(只测到千位，未考虑千位以后)
	 * @throws IOException
	 */
	public final static String getChainNumByNum(int num){
		String result="";
		if(num<0){
			result+="负";
			num=Math.abs(num);
		}
		if(num==0){
			return "零";
		}

		// 单位数组  
        String[] units = new String[] {"个","十","百","千","万","十","百","千","亿","十","百","千","万"};  
        // 中文大写数字数组  
        String[] numeric = new String[] {"零","一","二","三","四","五","六","七","八","九"};  
        char[] temp = String.valueOf(num).toCharArray();
        String res="";
        int nowNum=0;
        for (int k=0,lastIndex=temp.length-1;lastIndex>=0;k++,lastIndex--){
        	nowNum=Integer.parseInt(String.valueOf(temp[lastIndex]));
        	res=numeric[nowNum];
        	if(k>0&&nowNum>0){
        		res+=units[k];
        	}
            result=res+result;
        }
        if(result.startsWith("一十")){
        	result=result.substring(1);
        }
        while (result.endsWith("零")||result.contains("零零")) {
        	if(result.endsWith("零")){
        		result=result.substring(0,result.length()-1);
        	}
        	
        	if(result.contains("零零")){
        		result=result.replaceAll("零零","零");
        	}
		}
        
        return result;
	}
	/**
	 * 校验身份证
	 * @param certificateCode
	 * @return
	 */
	public static boolean checkCertificateCode(String certificateCode,List<String> msgs) {
		if(msgs==null){
			msgs=new ArrayList<String>();
		}
		// 输入号码不能为空
		if (certificateCode==null||certificateCode.length()==0) {
			msgs.add("输入的号码为空!");
			return false;
		}
		// 输入的长度应为15或18位
		if (certificateCode.length()!=15&&certificateCode.length()!=18) {
			msgs.add("输入的号码长度不对,身份证号码为15或是18位!");
			return false;
		}
		// 15位身份证校验
		if (certificateCode.length()==15) {
			// 输入的号码都应为数字
			if(!isDigit(certificateCode)) {
				msgs.add(msgs.size(),"输入的15位身份证中有非数字的字符!");
			}
			// 检查身份证月份和日期区间
			int y = Integer.parseInt("19"+certificateCode.substring(6, 8));
			int m = Integer.parseInt(certificateCode.substring(8, 10));
			int d = Integer.parseInt(certificateCode.substring(10, 12));
			if (!isBetween(m,1,12)) {
				msgs.add(msgs.size(),"15位身份证的月份超限，请检查第9，10位!");
			}
			Calendar c = Calendar.getInstance();
	        c.set(Calendar.YEAR, y);
	        c.set(Calendar.MONTH,m-1);
			if (!isBetween(d,1,c.getActualMaximum(Calendar.DAY_OF_MONTH))) {
				msgs.add(msgs.size(),"15位身份证的日期超限！请检查第11，12位!");
			}
			if(msgs.size()>0){
				return false;
			}
			return true;
		}
		// 18位身份证校验
		if (certificateCode.length()==18) {
			// 输入的号码都应为数字
			String prefix = certificateCode.substring(0, 17);
			char verifyCode = doVerifyIdCardNo(prefix);
			String s = certificateCode.substring(17, 18);
			if (!(verifyCode+"").equalsIgnoreCase(s)) {
				msgs.add("18位身份证,身份证号码不正确!");
				return false;
			}
		}
		return true;
	}
   private static char doVerifyIdCardNo(String idCardNo){
        char[] pszSrc=idCardNo.toCharArray();;
        int s = 0;
        int[] w=new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        char[] szVerCode = new char[]{'1','0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        for(int i=0;i<17;i++){
            s += (int)(pszSrc[i]-'0')* w[i];
        }
        int iY = s%11;
        return szVerCode[iY];
    }
	/**
	 * 检查是否是数字字符串
	 * @param str
	 * @return
	 */
	public static boolean isDigit(String str) {
		String theNum = "0123456789";
		if(StringUtils.isBlank(str)){
		    return false;
		}else if(str.length()==1&&theNum.indexOf(str) == -1){
		    return false;
		}else {
		    for(char c:str.toCharArray()){
		        if (theNum.indexOf(c) == -1) {
		            return false;
		        }
		    }
		}
		return true;
	}
	/**
	 * 判断数字是否在俩者之间
	 * @param val
	 * @param lo
	 * @param hi
	 * @return
	 */
	public static boolean isBetween(int val,int lo,int hi) {
		if ((val<lo)||(val>hi)) {
			return false;
		} else {
			return true;
		}
	}
	/**
	 * 读取类路径下文件 以指定字符集读取类路径下的文件内容为字符串
	 * @param file 类路径文件路径
	 * @param charset
	 * @return
	 * @throws IOException 
	 */
	public static final String readClasspathFile(String file,String charset) throws IOException{
		InputStream in = null;
		ByteArrayOutputStream bos = null;
		try{
			in = Utils.class.getClassLoader().getResourceAsStream(file);
			byte[] buffer = new byte[1024*10];
			int len = -1;
			bos = new ByteArrayOutputStream();
			
			while ( (len = in.read(buffer))>-1){
				bos.write(buffer, 0, len);
			}
			byte[] data = bos.toByteArray();
			return new String(data,charset);
		}finally{
			clean(in,bos);
		}
	}
	/**
	 * 序列化对象  将序列化对象序 列化成字节数组 并base64编码
	 * @param obj
	 * @return
	 */
	public static String saveSerializableObjAsStr(Serializable obj){
		ByteArrayOutputStream bos = null ;
		ObjectOutputStream oo = null;
		try{
			bos = new ByteArrayOutputStream();
			oo = new ObjectOutputStream(bos);
			oo.writeObject(obj);
			oo.flush();
			byte[] data = bos.toByteArray();
			Base64Encoder coder = new Base64Encoder();
			return coder.encode(data);
			
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		finally{
			clean(bos,oo);
		}
	}
	/**
	 * 反序列化  将序列化后的base64编码字符串 反序列化成对象
	 * @param base64Str
	 * @return
	 */
	public static Serializable readSerializableObjFromStr(String base64Str){
		ObjectInputStream ooin = null;
		ByteArrayInputStream bin = null;
		try{
		    Base64Encoder coder = new Base64Encoder();
			byte[] data =  coder.decode(base64Str);
			bin = new ByteArrayInputStream(data);
			ooin = new ObjectInputStream(bin);
			return  (Serializable) ooin.readObject();
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		finally{
			clean(ooin,bin);
		}
	}
	/**
	 * 关闭流、链接等
	 * @param resources
	 */
	public static void clean(Object...resources) {
        for(Object o :resources){
            if(o==null){
                continue;
            }
            if (o instanceof OutputStream){
            
                try {
                    ((OutputStream)o).close();
                } catch (IOException e) {
                    
                    e.printStackTrace();
                }
            }
            else if (o instanceof InputStream){
                
                try {
                    ((InputStream)o).close();
                } catch (IOException e) {
                    
                    e.printStackTrace();
                }
            }else if (o instanceof Connection){
            
                try {
                    ((Connection)o).close();
                } catch (Exception e) {
                    
                    e.printStackTrace();
                }
            }else if (o instanceof ResultSet){
                
                try {
                    ((ResultSet)o).close();
                } catch (Exception e) {
                    
                    e.printStackTrace();
                }
            }else if (o instanceof ResultSet){
                
                try {
                    ((ResultSet)o).close();
                } catch (Exception e) {
                    
                    e.printStackTrace();
                }
            }else if (o instanceof Statement){
                
                try {
                    ((Statement)o).close();
                } catch (Exception e) {
                    
                    e.printStackTrace();
                }
            } else if (o instanceof Closeable){
            	try {
					((Closeable) o).close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
                 
        }
    }
	/**
	 * 将内容转换成html显示字符串
	 * @param content 内容
	 * @param length 截取长度
	 * @param wrap 段落标签
	 * @param endAppendHtml 最后一段追加的html
	 * @return
	 */
    public static String convertToHtmlStr(String content,int length,String wrap,String endAppendHtml){
        if(StringUtils.isNotBlank(content)){
            if(StringUtils.isBlank(wrap)){
                wrap="br";
            }
            if(length>0&&content.length()>length){
                content = Utils.escapeHtml(content.substring(0,length));
            }else{
                content = Utils.escapeHtml(content);
            }
            List<String> paragraphs=Utils.splitString(content,"\r\n");
            StringBuffer sb = new StringBuffer();
            int lastIndex=paragraphs.size()-1;
            for(int i=0;i<paragraphs.size();i++){
                if(i<lastIndex&&!"br".equals(wrap)){
                    sb.append("<").append(wrap).append(">");
                }
                sb.append(paragraphs.get(i));
                if(i<lastIndex){
                    if(!"br".equals(wrap)){
                        sb.append("</").append(wrap).append(">");
                    }else{
                        sb.append("<br/>");
                    } 
                }else if(StringUtils.isNotBlank(endAppendHtml)){
                    sb.append(endAppendHtml);
                }
            }
            content = sb.toString().replaceAll("[ ]", "&nbsp;").replaceAll("[　]", "&nbsp;&nbsp;");
        }
        
        return content;
    }
    /**
     * 生成uuid
     * @param rep
     * @return
     */
    public static final String getUUID(String rep){
        if(rep==null){
            rep="";
        }
        return UUID.randomUUID().toString().replaceAll("-", rep);
    }
    /**
     * 根据格式生成字符串流水号
     * @param strNoFormat
     * YYYY 代表年（不区分大小写）
     * MM 代表月（不区分大小写）
     * DD 代表日（不区分大小写）
     * ### 代表文号
     * [000] 代表三位文号（需要使用中括号）
     * @return
     */
    public static String formatStrNoByStrNoFormat(String strNoFormat,Date genDate,long no) {
        if(strNoFormat==null){
            strNoFormat="";
        }
        if(StringUtils.isBlank(strNoFormat)){
            return no+"";
        }
        
        String articleNo=strNoFormat;
        
        Integer year=null,month=null,day=null;
        if(genDate!=null){
            year=CalendarUtils.getYearByDate(genDate);
            month=CalendarUtils.getMonthByDate(genDate);
            day=CalendarUtils.getDayByDate(genDate);
        }
        //处理年
        if(year!=null){
            articleNo=RegExUtils.replacePattern(articleNo, "[Yy]{4}", year+"");
            articleNo=RegExUtils.replacePattern(articleNo, "[Yy]{2}", (year+"").substring(2,4));
        }
        //处理月
        if(month!=null) {
            String monthStr=month+"";
            if (month<10){
                monthStr="0"+monthStr;
            }
            articleNo=RegExUtils.replacePattern(articleNo, "[Mm]{2}", monthStr+"");
            articleNo=RegExUtils.replacePattern(articleNo, "[Mm]{1}", month+"");
        }
        //处理日
        if(day!=null) {
            String dateStr=day+"";
            if (day<10){
                dateStr="0"+dateStr;
            }
            articleNo = RegExUtils.replacePattern(articleNo, "[Dd]{2}", dateStr+"");
            articleNo = RegExUtils.replacePattern(articleNo, "[Dd]{1}", day+"");
        }
        //处理中文流水号
        articleNo=RegExUtils.replacePattern(articleNo,"zh#",Utils.getChainNumByNum(new Long(no).intValue()));
        //处理流水号
        String maxNumFmt="#";//最长流水号格式
        while(articleNo.indexOf(maxNumFmt)>-1){
            maxNumFmt+="#";
        }
        while(maxNumFmt.length()>0){
            articleNo=RegExUtils.replaceAll(articleNo, maxNumFmt, Utils.lpadStr(no+"", maxNumFmt.length(), '0'));
            maxNumFmt=maxNumFmt.substring(1);
        }
        
        return articleNo;
    }
    /**
     * 根据格式生成字符串流水号
     * @param strNoFormat
     * YYYY 代表年（不区分大小写）
     * MM 代表月（不区分大小写）
     * DD 代表日（不区分大小写）
     * ### 代表文号
     * [000] 代表三位文号（需要使用中括号）
     * @return
     */
    public static Integer unFormatStrNoByStrNoFormat(String strNoFormat, Date genDate, String strNo) {
        Integer no=null;
        if(strNoFormat==null){
            strNoFormat="";
        }
        strNoFormat=strNoFormat.toLowerCase();
        if(strNoFormat.indexOf("#")<0){
            return null;
        }
        
        Integer year=null,month=null,day=null;
        if(genDate!=null){
            year=CalendarUtils.getYearByDate(genDate);
            month=CalendarUtils.getMonthByDate(genDate);
            day=CalendarUtils.getDayByDate(genDate);
        }
        //处理年格式化
        String yearFmt="yyyy";
        int yearBeginIdx=strNoFormat.indexOf(yearFmt);
        while(yearBeginIdx>-1){
            int yearEndSubStrIdx=yearBeginIdx+yearFmt.length();
            if(year!=null){
                Integer noYear=null;
                try {
                    noYear=Integer.valueOf(strNo.substring(yearBeginIdx, yearEndSubStrIdx));
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("格式错误");
                }
                if(year.intValue()!=noYear.intValue()){
                    throw new RuntimeException("格式年份错误");
                }
            }
            strNoFormat=strNoFormat.substring(0,yearBeginIdx)+strNoFormat.substring(yearEndSubStrIdx);
            strNo=strNo.substring(0,yearBeginIdx)+strNo.substring(yearEndSubStrIdx);
            
            yearBeginIdx = strNoFormat.indexOf(yearFmt);
            if(yearBeginIdx<0&&"yyyy".equals(yearFmt)){
                yearFmt="yy";//先处理yyyy再处理yy
                year=Integer.valueOf((year+"").substring(2));
                yearBeginIdx = strNoFormat.indexOf(yearFmt);
            }
        }
        //处理月格式化
        String monthFmt="mm";
        int monthBeginIdx=strNoFormat.indexOf(monthFmt);
        while(monthBeginIdx>-1){
            int monthEndSubStrIdx=monthBeginIdx+monthFmt.length();
            if(month!=null){
                Integer noMonth=null;
                try {
                    noMonth=Integer.valueOf(strNo.substring(monthBeginIdx, monthEndSubStrIdx));
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("格式错误");
                }
                if(month.intValue()!=noMonth.intValue()){
                    throw new RuntimeException("格式月份错误");
                }
            }
            strNoFormat=strNoFormat.substring(0,monthBeginIdx)+strNoFormat.substring(monthEndSubStrIdx);
            strNo=strNo.substring(0,monthBeginIdx)+strNo.substring(monthEndSubStrIdx);
            
            monthBeginIdx = strNoFormat.indexOf(monthFmt);
            if(monthBeginIdx<0&&"mm".equals(monthFmt)){
                monthFmt="m";//先处理mm再处理m
                monthBeginIdx = strNoFormat.indexOf(monthFmt);
            }
        }
        //处理日格式化
        String dayFmt="dd";
        int dayBeginIdx=strNoFormat.indexOf(dayFmt);
        while(dayBeginIdx>-1){
            int dayEndSubStrIdx=dayBeginIdx+dayFmt.length();
            if(month!=null){
                Integer noDay=null;
                try {
                    noDay=Integer.valueOf(strNo.substring(dayBeginIdx, dayEndSubStrIdx));
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("格式错误");
                }
                if(day.intValue()!=noDay.intValue()){
                    throw new RuntimeException("格式日错误");
                }
            }
            strNoFormat=strNoFormat.substring(0,dayBeginIdx)+strNoFormat.substring(dayEndSubStrIdx);
            strNo=strNo.substring(0,dayBeginIdx)+strNo.substring(dayEndSubStrIdx);
            
            dayBeginIdx = strNoFormat.indexOf(dayFmt);
            if(dayBeginIdx<0&&"dd".equals(dayFmt)){
                dayFmt="d";//先处理dd再处理d
                dayBeginIdx = strNoFormat.indexOf(dayFmt);
            }
        }
        
        //处理流水号
        int zhNoIdx=strNoFormat.indexOf("zh#");//最长流水号格式
        int noIdx=strNoFormat.indexOf("#");
        if(zhNoIdx>-1&&zhNoIdx<noIdx){
            int zhNoEndIdx=zhNoIdx+2;
            //_倒序查找
            noIdx=strNoFormat.lastIndexOf("#");
            int i=1;
            while(strNoFormat.charAt(noIdx-i)=='#'&&zhNoEndIdx<(noIdx-i)){
                i++;
            }
            int noEndIdx=strNo.length()-((noIdx+1)==strNoFormat.length()?0:strNoFormat.substring(noIdx+1).length());
            try {
                no=Integer.valueOf(strNo.substring(noEndIdx-i,noEndIdx));
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("流水号格式错误");
            }
        }else{
            //正序查找
            int i=1;
            while(strNoFormat.charAt(noIdx+i)=='#'){
                i++;
            }
            try {
                no=Integer.valueOf(strNo.substring(noIdx, noIdx+i));
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("流水号格式错误");
            }
        }
        
        return no;
    }
    /**
     * 语义上的相等
     * @param o1
     * @param o2
     * @return
     */
    public static boolean isSameObj(Object o1,Object o2){
        if(o1==null){
            if(o2==null){
                return true;
            }else if(o2 instanceof String){
                return StringUtils.isBlank((String)o2);
            }else{
                return false;
            }
        }else{
            if(o2==null){
                if(o1 instanceof String){
                    return StringUtils.isBlank((String)o1);
                }else{
                    return false;
                }
            }else{
                return o1.toString().equals(o2.toString());
            }
        }
    }
    
    /**
     * 下载文件
     * @param downloadUrl 下载路径
     * @param downloadDir 下载存放目录
     * @param fileName 文件名称
     * @return 返回下载文件
     */
    public static File downloadFile(String downloadUrl, String downloadDir, String fileName) {
        File file = null;
        try {
            URL url = new URL(downloadUrl);// 统一资源
            URLConnection urlConnection = url.openConnection();// 连接类的父类，抽象类
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection; // http的连接类
            httpURLConnection.setRequestMethod("GET");// 设定请求的方法，默认是GET
            httpURLConnection.setConnectTimeout(10000);// 连接超时时间 10秒
            httpURLConnection.setReadTimeout(30000);// 读取数据时间30秒
            httpURLConnection.setRequestProperty("Charset", "UTF-8");// 设置字符编码
            httpURLConnection.connect();// 打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）。
            BufferedInputStream bin = new BufferedInputStream(httpURLConnection.getInputStream());
            String path = downloadDir+"/"+fileName;
            file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if(!file.exists()){
            	file.createNewFile();
            }
            OutputStream out = new FileOutputStream(file);
            int size = 0;
            byte[] buf = new byte[1024];
            while ((size = bin.read(buf)) != -1) {
                out.write(buf, 0, size);
            }
            bin.close();
            out.flush();
            out.close();
            httpURLConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("文件下载失败"+e.getMessage());
        }
        return file;
    }
    
    /**
     * 过滤表情符号
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {
		if (source != null) {
			Pattern emoji = Pattern
					.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
							Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
			Matcher emojiMatcher = emoji.matcher(source);
			if (emojiMatcher.find()) {
				source = emojiMatcher.replaceAll("?");
				return source;
			}
			return source;
		}
		return source;
	}
    /**
     * 获取jpa代理类真正的类名
     * @param prjType
     * @return
     */
    public static final String getProxyObjClassName(String prjType) {
        if(StringUtils.isNotBlank(prjType)) {
        	int idx = prjType.indexOf("_$");
        	if(idx>-1){
        		return prjType.substring(0, idx);
        	}else{
        		idx = prjType.indexOf("$");
                if(idx>-1) {
                	return prjType.substring(0, idx);
                }else {
                    return prjType;
                }
        	}
            
        }else {
            return prjType;
        }
    }

	/**
	 * 连接字符串
	 * 将List变为字符串
	 * @param list
	 * @param regex
	 * @return
	 */
	public static String collectionToString(Collection<String> list , String regex ){

		StringBuffer sb = new StringBuffer();
		boolean off = false ;
		for(String str : list){
			if(off){
				sb.append(regex);
			}else{
				off = true ;
			}
			sb.append(str);
		}

		return sb.toString() ;
	}

    /**
     * 获取异常的堆栈信息
     * @param throwable
     * @return
     */
    public static String getStackTrace(Throwable throwable){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        try{
            throwable.printStackTrace(pw);
            return sw.toString();
        } finally{
            pw.close();
        }
    }

	/**
	 * 去掉url中的路径，留下请求参数部分
	 * @param strURL url地址
	 * @return url请求参数部分
	 * @author lzf
	 */
	private static String TruncateUrlPage(String strURL){
		String strAllParam=null;
		String[] arrSplit=null;
		strURL=strURL.trim().toLowerCase();
		arrSplit=strURL.split("[?]");
		if(strURL.length()>1){
			if(arrSplit.length>1){
				for (int i=1;i<arrSplit.length;i++){
					strAllParam = arrSplit[i];
				}
			}
		}
		return strAllParam;
	}

	/**
	 * 解析出url参数中的键值对
	 * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
	 * @param URL  url地址
	 * @return  url请求参数部分
	 * @author lzf
	 */
	public static Map<String, String> urlSplit(String URL){
		Map<String, String> mapRequest = new HashMap<String, String>();
		String[] arrSplit=null;
		String strUrlParam=TruncateUrlPage(URL);
		if(strUrlParam==null){
			return mapRequest;
		}
		arrSplit=strUrlParam.split("[&]");
		for(String strSplit:arrSplit){
			String[] arrSplitEqual=null;
			arrSplitEqual= strSplit.split("[=]");
			//解析出键值
			if(arrSplitEqual.length>1){
				//正确解析
				mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
			}else{
				if(arrSplitEqual[0]!=""){
					//只有参数没有值，不加入
					mapRequest.put(arrSplitEqual[0], "");
				}
			}
		}
		return mapRequest;
	}


	/**
	 * 获得包下面的所有的class
	 *
	 * @param pack
	 *            package完整名称
	 * @return List包含所有class的实例
	 */
	public static List<Class> getClassesFromPackage(String pack) {
		List<Class> clazzs = new ArrayList<Class>();

		// 是否循环搜索子包
		boolean recursive = true;

		// 包名字
		String packageName = pack;
		// 包名对应的路径名称
		String packageDirName = packageName.replace('.', '/');

		Enumeration<URL> dirs;

		try {
			dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();

				String protocol = url.getProtocol();

				if ("file".equals(protocol)) {
					System.out.println("file类型的扫描");
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					findClassInPackageByFile(packageName, filePath, recursive, clazzs);
				} else if ("jar".equals(protocol)) {
					System.out.println("jar类型的扫描");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return clazzs;
	}

	/**
	 * 在package对应的路径下找到所有的class
	 *
	 * @param packageName
	 *            package名称
	 * @param filePath
	 *            package对应的路径
	 * @param recursive
	 *            是否查找子package
	 * @param clazzs
	 *            找到class以后存放的集合
	 */
	public static void findClassInPackageByFile(String packageName, String filePath, final boolean recursive, List<Class> clazzs) {
		File dir = new File(filePath);
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		// 在给定的目录下找到所有的文件，并且进行条件过滤
		File[] dirFiles = dir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				boolean acceptDir = recursive && file.isDirectory();// 接受dir目录
				boolean acceptClass = file.getName().endsWith("class");// 接受class文件
				return acceptDir || acceptClass;
			}
		});

		for (File file : dirFiles) {
			if (file.isDirectory()) {
				findClassInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive, clazzs);
			} else {
				String className = file.getName().substring(0, file.getName().length() - 6);
				try {
					clazzs.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + "." + className));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static FileItem createFileItem(File file) {
		FileItemFactory factory = new DiskFileItemFactory(16, null);
		FileItem item = factory.createItem("uploadFile", "multipart/form-data", true, file.getName());
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		try {
			FileInputStream fis = new FileInputStream(file);
			OutputStream os = item.getOutputStream();
			while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return item;
	}
}

