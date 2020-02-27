package com.sys.monitor.test.service;

import com.sys.monitor.service.FileService;
import com.sys.monitor.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

/**
 * @Author willis
 * @desc
 * @since 2020年02月27日 16:45
 */
public class FileServiceTest extends BaseTest {
    @Autowired
    private FileService fileService;

    @Test
    public void uploadTest() {
        String token = "qE9bPqVcqrjIEFEt6ghtRXa5LVNHMD2POg6XITtf:BXOW0sqFnXdqIPJtiUAz8yvm_Tk=:eyJpbnNlcnRPbmx5IjoxLCJlbmRVc2VyIjoiMjAiLCJzYXZlS2V5IjoidGVzdC9idXNpbmVzcy9kZGpqLyQoZXRhZykkKGV4dCkiLCJkZXRlY3RNaW1lIjoxLCJjYWxsYmFja0JvZHlUeXBlIjoiYXBwbGljYXRpb24vanNvbiIsInNjb3BlIjoib3NzLXByaXZhdGUiLCJtaW1lTGltaXQiOiIhdGV4dC9odG1sO2FwcGxpY2F0aW9uL2phdmFzY3JpcHQ7dGV4dC9qYXZhc2NyaXB0O2ltYWdlL3N2Zyt4bWw7dGV4dC9jc3MiLCJjYWxsYmFja1VybCI6Imh0dHA6Ly9xbmZzLmZhbmdkZC5uZXQvY2FsbGJhY2svcWluaXUtdXBsb2FkP2lkXHUwMDNkZGRqai0yMDAxMy0xNTgyNzkzMzI5MDY2IiwiZm9yY2VTYXZlS2V5Ijp0cnVlLCJkZWFkbGluZSI6MTU4Mjc5NjkyOSwiY2FsbGJhY2tCb2R5Ijoie1wiYnVja2V0XCI6XCIkKGJ1Y2tldClcIixcImNsaWVudElkXCI6XCJkZGpqXCIsXCJldGFnXCI6XCIkKGV0YWcpXCIsXCJrZXlcIjpcIiQoa2V5KVwiLFwibWltZVwiOlwiJChtaW1lVHlwZSlcIixcInBvbGljeVwiOjIwMDEzfSJ9";
        File file = new File("E:\\7c86750dc48c0e07809e7f7cc137dc5400dc538cffd66dc7dc6ab117d5c765ea.xls");
        String s = fileService.upload2SevenOx(file, token);
    }
}
