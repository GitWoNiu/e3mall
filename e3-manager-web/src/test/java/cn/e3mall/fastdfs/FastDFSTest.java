package cn.e3mall.fastdfs;

import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

public class FastDFSTest {

	@Test
	public void testUpLoadFile() throws Exception {
		//1.加载配置文件
		ClientGlobal.init("D:/JavaSW/Git-e3mall-repository/e3-manager-web/src/main/resources/conf/client.cnf");
		//2.创建一个TrackerClient对象
		TrackerClient trackerClient = new TrackerClient();
		//3.获取TrackerServer对象
		TrackerServer trackerServer = trackerClient.getConnection();
		//4.创建一个StorageClient对象
		StorageClient storageClient = new StorageClient(trackerServer, null);
		//5.使用storageClient上传文件，返回文件的路径及文件名
		String[] strings = storageClient.upload_file("E:/Test01/photo.jpg", "jpg", null);
		//6.输出结果
		for (String string : strings) {
			System.out.println(string);
		}
	}
}
