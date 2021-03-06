package com.yahoo.vespa.hosted.node.admin.task.util.file;

import com.yahoo.vespa.hosted.node.admin.component.TaskContext;
import com.yahoo.vespa.test.file.TestFileSystem;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileSystem;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class FileDeleterTest {
    private final FileSystem fileSystem = TestFileSystem.create();
    private final UnixPath path = new UnixPath(fileSystem.getPath("/tmp/foo"));
    private final FileDeleter deleter = new FileDeleter(path.toPath());
    private final TaskContext context = mock(TaskContext.class);

    @Test
    public void deleteExisting() throws IOException {
        assertFalse(deleter.converge(context));
        path.createParents().writeUtf8File("bar");
        assertTrue(deleter.converge(context));
        assertFalse(deleter.converge(context));
    }
}