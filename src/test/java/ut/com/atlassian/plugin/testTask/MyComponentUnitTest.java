package ut.com.atlassian.plugin.testTask;

import org.junit.Test;
import com.atlassian.plugin.testTask.api.MyPluginComponent;
import com.atlassian.plugin.testTask.api.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}