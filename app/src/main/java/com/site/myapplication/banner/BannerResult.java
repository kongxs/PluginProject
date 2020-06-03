package com.site.myapplication.banner;

import java.util.List;
import java.util.Objects;

public class BannerResult {

    public List<ResultBean> bannerList;

    public static class ResultBean {

        public String advertImageUrl;
        public String transferUrl;
        String positionKey;
        int transferType;
        String sku;

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (this.getClass() != obj.getClass()) {
                return false;
            }
            if (!(obj instanceof ResultBean)) {
                return false;
            }
            ResultBean otherResultBean = (ResultBean) obj;
            return equalsCompact(advertImageUrl, otherResultBean.advertImageUrl)
                    && equalsCompact(transferUrl, otherResultBean.transferUrl)
                    && equalsCompact(positionKey, otherResultBean.positionKey)
                    && transferType == otherResultBean.transferType;
        }

        static boolean equalsCompact(Object a, Object b) {
            return Objects.equals(a, b);
        }

    }
}