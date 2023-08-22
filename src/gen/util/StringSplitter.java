package gen.util;

import java.util.ArrayList;
import java.util.List;

public class StringSplitter
{

    private StringSplitter()
    {
        // nil
    }

    public enum StringType
    {
        NORMAL,
        KEYWORD;
    }

    /**
     * Splits a String into a list of Strings.
     * The Strings are still in order, but the StringType marks all keywords.
     * So "ABC" with List.of("B") will be {[NORMAL, "A"], [KEYWORD, "B"], [NORMAL,
     * "C"]}
     */
    public static List<Tuple<StringType, String>> splitForKeywords(String text, List<String> keywordList)
    {
        List<Tuple<StringType, String>> list = new ArrayList<>();
        if(text == null)
            return list;
        list.add(new Tuple<>(StringType.NORMAL, text));
        List<Tuple<StringType, String>> tempList = new ArrayList<>();
        for (String keyword : keywordList)
        {
            for (var tuple : list)
            {
                if (tuple.x() == StringType.KEYWORD)
                {
                    tempList.add(tuple);
                    continue;
                }
                boolean start = tuple.y().startsWith(keyword);
                boolean end = tuple.y().endsWith(keyword);
                String[] split = tuple.y().split(keyword);
                if (start)
                    tempList.add(new Tuple<>(StringType.KEYWORD, keyword));
                for (int i = 0; i < split.length; i++)
                {
                    tempList.add(new Tuple<>(StringType.NORMAL, split[i]));
                    if (i < split.length - 1)
                    {
                        tempList.add(new Tuple<>(StringType.KEYWORD, keyword));
                    }
                }
                if (end)
                    tempList.add(new Tuple<>(StringType.KEYWORD, keyword));
            }
            list.clear();
            list.addAll(tempList);
            tempList.clear();
        }
        return list;
    }
}
