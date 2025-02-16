public class Analyzer {

    interface TextAnalyzer {
        Label processText(String text);
    }

    enum Label {
        OK,
        SPAM,
        NEGATIVE_TEXT,
        TOO_LONG,
        TOO_MUCH_KEYWORDS
    }

    abstract static class KeywordAnalyzer implements TextAnalyzer {
        abstract String[] getKeywords();

        abstract Label getLabel();

        @Override
        public Label processText(String text) {
            for (String keyword : getKeywords()) {
                if (text.contains(keyword)) {
                    return getLabel();
                }
            }
            return Label.OK;
        }
    }

    static class SpamAnalyzer extends KeywordAnalyzer {
        private final String[] keywords;

        public SpamAnalyzer(String[] keywords) {
            this.keywords = keywords.clone();
        }

        @Override
        protected String[] getKeywords() {
            return keywords;
        }

        @Override
        protected Label getLabel() {
            return Label.SPAM;
        }
    }

    static class NegativeTextAnalyzer extends KeywordAnalyzer {
        private final String[] keywords;

        public NegativeTextAnalyzer() {
            this.keywords = new String[]{":(", "=(", ":|"};
        }

        @Override
        protected String[] getKeywords() {
            return keywords;
        }

        @Override
        protected Label getLabel() {
            return Label.NEGATIVE_TEXT;
        }
    }

    static class TooLongTextAnalyzer implements TextAnalyzer {
        private final int maxLength;

        public TooLongTextAnalyzer(int maxLength) {
            this.maxLength = maxLength;
        }

        @Override
        public Label processText(String text) {
            if (text.length() > maxLength) {
                return Label.TOO_LONG;
            }
            return Label.OK;
        }
    }


    static class TooMuchKeywordsAnalyzer extends KeywordAnalyzer {
        private final String[] keyword;
        private final int maxCount;


        public TooMuchKeywordsAnalyzer(String[] keyword, int maxCount) {
            this.keyword = keyword.clone();
            this.maxCount = maxCount;

        }

        @Override
        protected String[] getKeywords() {
            return keyword;
        }

        @Override
        protected Label getLabel() {
            return Label.TOO_MUCH_KEYWORDS;
        }

        @Override
        public Label processText(String text) {
            String[] words = text.split("[^\\p{L}0-9]+");
            for (String keywords : keyword) {
                int count = 0;
                for (String word : words) {
                    if (word.equalsIgnoreCase(keywords)) {
                        count++;
                    }
                }
                if (count > maxCount) {
                    return getLabel();
                }
            }
            return Label.OK;
        }
    }


    public static void main(String[] args) {
        TextAnalyzer[] analyzers = new TextAnalyzer[]{
                new TooMuchKeywordsAnalyzer(new String[]{"джава"}, 3)
        };

        String text1 = "я джава, я учу людей джава, джава чертовски хорош, джава это куча денег, джава";
        String text2 = "джава это круто";

        for (TextAnalyzer analyzer : analyzers) {
            System.out.println("Результат анализа текста 1: " + analyzer.processText(text1));
            System.out.println("Результат анализа текста 2: " + analyzer.processText(text2));
        }
    }
}



