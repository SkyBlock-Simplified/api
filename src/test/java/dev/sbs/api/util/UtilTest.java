package dev.sbs.api.util;

import dev.sbs.api.SimplifiedApi;
import dev.sbs.api.data.model.skyblock.items.ItemModel;
import dev.sbs.api.data.sql.SqlConfig;
import dev.sbs.api.util.collection.concurrent.ConcurrentList;
import dev.sbs.api.util.helper.StringUtil;
import dev.sbs.api.util.helper.SystemUtil;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

@SuppressWarnings("all")
public class UtilTest {

    @Test
    public void genericTest_ok() {
        String javaVersion = SystemUtil.JAVA_VERSION;
        String javaSpecVersion = SystemUtil.JAVA_SPECIFICATION_VERSION;
        String uValue = "\\u2699\\u2699";
        char uValue2 = '\u2699';

        String emoji = StringUtil.unescapeUnicode(uValue);
        String output = StringUtil.escapeUnicode(emoji);

        MatcherAssert.assertThat("Done", true);
    }

    @Test
    public void searchTest_ok() {
        System.out.println("Database Starting... ");
        SimplifiedApi.getSessionManager().connect(SqlConfig.defaultSql());
        System.out.println("Database initialized in " + SimplifiedApi.getSessionManager().getSession().getInitializationTime() + "ms");
        System.out.println("Database started in " + SimplifiedApi.getSessionManager().getSession().getStartupTime() + "ms");
        String accessoryId = "WITHER_RELIC";

        long before1 = ctm();
        ConcurrentList<ItemModel> result1 = SimplifiedApi.getRepositoryOf(ItemModel.class).findAll();
        System.out.println("1.1 took " + (ctm() - before1));

        long before12 = ctm();
        ConcurrentList<ItemModel> result12 = SimplifiedApi.getRepositoryOf(ItemModel.class).findCached();
        System.out.println("1.2 took " + (ctm() - before12));

        long before2 = ctm();
        ItemModel result2 = result1.findFirstOrNull(ItemModel::getItemId, accessoryId);
        System.out.println("2.1 took " + (ctm() - before2));

        long before22 = ctm();
        ItemModel result22 = result12.findFirstOrNull(ItemModel::getItemId, accessoryId);
        System.out.println("2.2 took " + (ctm() - before22));

        long before4 = ctm();
        ItemModel model4 = SimplifiedApi.getRepositoryOf(ItemModel.class).findFirstOrNull(ItemModel::getItemId, accessoryId);
        System.out.println("4.1 took " + (ctm() - before4));

        long before42 = ctm();
        ItemModel model42 = SimplifiedApi.getRepositoryOf(ItemModel.class).findAll().findFirstOrNull(ItemModel::getItemId, accessoryId);
        System.out.println("4.2 took " + (ctm() - before42));

        long before43 = ctm();
        ItemModel model43 = SimplifiedApi.getRepositoryOf(ItemModel.class).findCached().findFirstOrNull(ItemModel::getItemId, accessoryId);
        System.out.println("4.3 took " + (ctm() - before43));

        String test2 = "testing";
    }

    public static long ctm() {
        return System.currentTimeMillis();
    }

}
