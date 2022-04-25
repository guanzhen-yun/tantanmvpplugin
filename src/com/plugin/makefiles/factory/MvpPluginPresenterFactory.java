package com.plugin.makefiles.factory;

import com.plugin.makefiles.bean.MvpPluginMethodBean;
import com.plugin.makefiles.data.MvpPluginData;
import com.plugin.makefiles.utils.MvpPluginUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * mvp生成工厂--Presenter
 */
public class MvpPluginPresenterFactory extends MvpPluginClassFactory {
    @Override
    String getClassName() {
        return MvpPluginUtils.getPresenterName();
    }

    @Override
    List<String> getImportList() {
        List<String> list = new ArrayList<>();
        list.add(getEndNewString("import com.p1.mobile.android.app.LifecycleProvider"));
        list.add(getEndNewString("import com.p1.mobile.android.architec.impl.BasePresenter"));
        return list;
    }

    @Override
    String getClassNameLineStr() {
        if (MvpPluginData.isJava()) {
            return "public class " + MvpPluginUtils.getPresenterName() + " extends BasePresenter<" + MvpPluginUtils.getViewModelName() + "> {";
        } else {
            return "class " + MvpPluginUtils.getPresenterName() + "(lifecycleProvider: LifecycleProvider?) : BasePresenter<" + MvpPluginUtils.getViewModelName() + ">(lifecycleProvider) {";
        }
    }

    @Override
    List<String> getArgs() {
        return null;
    }

    @Override
    List<MvpPluginMethodBean> getMethodList() {
        List<MvpPluginMethodBean> methodBeanList = new ArrayList<>();
        if (MvpPluginData.isJava()) {
            //构造函数
            MvpPluginMethodBean constructBean = new MvpPluginMethodBean();
            constructBean.contents = getConstructContentList();
            methodBeanList.add(constructBean);
        }

        //initSubscription()
        MvpPluginMethodBean initSubscriptionBean = new MvpPluginMethodBean();
        initSubscriptionBean.contents = getInitSubscriptionContentList();
        methodBeanList.add(initSubscriptionBean);

        //destroy()
        MvpPluginMethodBean destroyBean = new MvpPluginMethodBean();
        destroyBean.contents = getDestroyContentList();
        methodBeanList.add(destroyBean);
        return methodBeanList;
    }

    private static List<String> getDestroyContentList() {
        List<String> listDestroy = new ArrayList<>();
        if (MvpPluginData.isJava()) {
            listDestroy.add("  @Override");
            listDestroy.add("  public void destroy() {}");
        } else {
            listDestroy.add("  override fun destroy() {}");
        }
        return listDestroy;
    }

    private static List<String> getInitSubscriptionContentList() {
        List<String> listInitSubscription = new ArrayList<>();
        if (MvpPluginData.isJava()) {
            listInitSubscription.add("  @Override");
            listInitSubscription.add("  public void initSubscription() {");
        } else {
            listInitSubscription.add("  override fun initSubscription() {");
        }
        listInitSubscription.add(getEndNewString("    super.initSubscription()"));
        listInitSubscription.add("  }");
        return listInitSubscription;
    }

    private static List<String> getConstructContentList() {
        List<String> listConstruct = new ArrayList<>();
        listConstruct.add("  public " + MvpPluginUtils.getPresenterName() + "(LifecycleProvider lifecycleProvider) {");
        listConstruct.add(getEndNewString("    super(lifecycleProvider)"));
        listConstruct.add("  }");
        return listConstruct;
    }
}
