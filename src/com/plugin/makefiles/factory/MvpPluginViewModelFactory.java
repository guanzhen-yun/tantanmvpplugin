package com.plugin.makefiles.factory;

import com.plugin.makefiles.bean.MvpPluginMethodBean;
import com.plugin.makefiles.data.MvpPluginData;
import com.plugin.makefiles.utils.MvpPluginUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * mvp生成工厂--ViewModel
 */
public class MvpPluginViewModelFactory extends MvpPluginClassFactory {
    @Override
    String getClassName() {
        return MvpPluginUtils.getViewModelName();
    }

    @Override
    List<String> getImportList() {
        List<String> list = new ArrayList<>();
        if (MvpPluginData.isJava()) {
            list.add(getEndNewString("import android.content.Context"));
        }
        if (MvpPluginData.isFragment()) {
            list.add(getEndNewString("import android.os.Bundle"));
        }
        list.add(getEndNewString("import android.view.LayoutInflater"));
        list.add(getEndNewString("import android.view.View"));
        list.add(getEndNewString("import android.view.ViewGroup"));
        list.add(getEndNewString("import androidx.annotation.Nullable"));
        if (MvpPluginData.isFragment()) {
            list.add(getEndNewString("import com.p1.mobile.android.app.Act"));
        }
        if (MvpPluginData.isMvp) {
            list.add(getEndNewString("import com.p1.mobile.android.architec.IViewModel"));
        }
        try {
            if (!"".equals(MvpPluginUtils.getXmlHolder())) {
                list.add(getEndNewString("import " + MvpPluginUtils.getHolderPathByModule() + "." + MvpPluginUtils.getViewModelName() + "_" + MvpPluginData.layoutName));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    String getClassNameLineStr() {
        if (MvpPluginData.isMvp) {
            if (MvpPluginData.isJava()) {
                return "public class " + MvpPluginUtils.getViewModelName() + " implements IViewModel<" + MvpPluginUtils.getPresenterName() + "> {";
            } else {
                if (MvpPluginData.isActivity()) {
                    return "class " + MvpPluginUtils.getViewModelName() + "(val act: " + MvpPluginData.cName + ") : IViewModel<" + MvpPluginUtils.getPresenterName() + "> {";
                } else {
                    return "class " + MvpPluginUtils.getViewModelName() + "(val act: Act, val frag: " + MvpPluginData.cName + ") : IViewModel<" + MvpPluginUtils.getPresenterName() + "> {";
                }
            }
        } else {
            if (MvpPluginData.isJava()) {
                return "public class " + MvpPluginUtils.getViewModelName() + " {";
            } else {
                if (MvpPluginData.isActivity()) {
                    return "class " + MvpPluginUtils.getViewModelName() + "(val act: " + MvpPluginData.cName + ") {";
                } else {
                    return "class " + MvpPluginUtils.getViewModelName() + "(val act: Act, val frag: " + MvpPluginData.cName + ") {";
                }
            }
        }
    }

    @Override
    List<String> getArgs() {
        List<String> args = new ArrayList<>();
        if (MvpPluginData.isActivity()) {
            if (MvpPluginData.isJava()) {
                args.add(getEndNewString("  private " + MvpPluginData.cName + " act"));
            }
        } else {
            if (MvpPluginData.isJava()) {
                args.add(getEndNewString("  private Act act"));
                args.add(getEndNewString("  private final " + MvpPluginData.cName + " frag"));
            }
        }
        if (MvpPluginData.isMvp) {
            if (MvpPluginData.isJava()) {
                args.add(getEndNewString("  private " + MvpPluginUtils.getPresenterName() + " presenter"));
            } else {
                args.add("  private lateinit var presenter: " + MvpPluginUtils.getPresenterName());
            }
        }
        if (args.size() == 0) {
            args = null;
        }
        return args;
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

        if (MvpPluginData.isMvp) {
            //bindPresenter()
            MvpPluginMethodBean bindPresenterBean = new MvpPluginMethodBean();
            bindPresenterBean.contents = getBindPresenterContentList();
            methodBeanList.add(bindPresenterBean);
        }

        //inflateView()
        MvpPluginMethodBean inflateViewBean = new MvpPluginMethodBean();
        inflateViewBean.contents = getInflateViewContentList();
        methodBeanList.add(inflateViewBean);

        try {
            if ("".equals(MvpPluginUtils.getXmlHolder())) {
                //initViews()方法
                MvpPluginMethodBean initViewsBean = new MvpPluginMethodBean();
                initViewsBean.contents = getInitViewsContentList();
                methodBeanList.add(initViewsBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (MvpPluginData.isMvp) {
            //destroy()方法
            MvpPluginMethodBean destroyBean = new MvpPluginMethodBean();
            destroyBean.contents = getDestroyContentList();
            methodBeanList.add(destroyBean);
        }

        //context() 方法
        MvpPluginMethodBean contextBean = new MvpPluginMethodBean();
        contextBean.contents = getContextContentList();
        methodBeanList.add(contextBean);

        if (MvpPluginData.isFragment()) {
            //initArguments()方法
            MvpPluginMethodBean initArgumentsBean = new MvpPluginMethodBean();
            initArgumentsBean.contents = getInitArgumentsContentList();
            methodBeanList.add(initArgumentsBean);
        }

        return methodBeanList;
    }

    private static List<String> getInitViewsContentList() {
        List<String> listInitViews = new ArrayList<>();
        if (MvpPluginData.isJava()) {
            listInitViews.add("  private void initViews(View view) {}");
        } else {
            listInitViews.add("  private fun initViews(view: View) {}");
        }
        return listInitViews;
    }

    private static List<String> getInitArgumentsContentList() {
        List<String> listInitArguments = new ArrayList<>();
        if (MvpPluginData.isJava()) {
            listInitArguments.add("  public void initArguments(Bundle arguments) {}");
        } else {
            listInitArguments.add("  fun initArguments(arguments: Bundle) {}");
        }
        return listInitArguments;
    }

    private static List<String> getContextContentList() {
        List<String> listContext = new ArrayList<>();
        listContext.add("  @Nullable");
        if (MvpPluginData.isJava()) {
            if (MvpPluginData.isMvp) {
                listContext.add("  @Override");
            }
            listContext.add("  public Context context() {");
            listContext.add(getEndNewString("    return act"));
            listContext.add("  }");
        } else {
            if (MvpPluginData.isMvp) {
                listContext.add("  override fun context() = act");
            } else {
                listContext.add("  fun context() = act");
            }
        }
        return listContext;
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

    private static List<String> getInflateViewContentList() {
        List<String> listInflateView = new ArrayList<>();
        if (MvpPluginData.isJava()) {
            if (MvpPluginData.isMvp) {
                listInflateView.add("  @Override");
            }
            listInflateView.add("  public View inflateView(LayoutInflater inflater, ViewGroup parent) {");
        } else {
            if (MvpPluginData.isMvp) {
                listInflateView.add("  override fun inflateView(inflater: LayoutInflater, parent: ViewGroup): View {");
            } else {
                listInflateView.add("  fun inflateView(inflater: LayoutInflater, parent: ViewGroup): View {");
            }
        }
        try {
            if (!"".equals(MvpPluginUtils.getXmlHolder())) {
                listInflateView.add(getEndNewString("    return __inflate_view_" + MvpPluginUtils.getLayoutName() + "(inflater, parent)"));
            } else {
                if (MvpPluginData.isJava()) {
                    listInflateView.add(getEndNewString("    View view = inflater.inflate(R.layout." + MvpPluginUtils.getLayoutName() + ", parent, false)"));
                } else {
                    listInflateView.add(getEndNewString("    val view = inflater.inflate(R.layout." + MvpPluginUtils.getLayoutName() + ", parent, false)"));
                }
                listInflateView.add(getEndNewString("    initViews(view)"));
                listInflateView.add(getEndNewString("    return view"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        listInflateView.add("  }");
        return listInflateView;
    }

    private static List<String> getBindPresenterContentList() {
        List<String> listBindPresenter = new ArrayList<>();
        if (MvpPluginData.isJava()) {
            listBindPresenter.add("  @Override");
            listBindPresenter.add("  public void bindPresenter(" + MvpPluginUtils.getPresenterName() + " presenter) {");
        } else {
            listBindPresenter.add("  override fun bindPresenter(presenter: " + MvpPluginUtils.getPresenterName() + ") {");
        }
        listBindPresenter.add(getEndNewString("    this.presenter = presenter"));
        listBindPresenter.add("  }");
        return listBindPresenter;
    }

    private static List<String> getConstructContentList() {
        List<String> listConstruct = new ArrayList<>();
        if (MvpPluginData.isActivity()) {
            listConstruct.add("  public " + MvpPluginUtils.getViewModelName() + "(" + MvpPluginData.cName + " act) {");
        } else {
            listConstruct.add("  public " + MvpPluginUtils.getViewModelName() + "(Act act, " + MvpPluginData.cName + " frag) {");
        }
        listConstruct.add(getEndNewString("    this.act = act"));
        if (MvpPluginData.isFragment()) {
            listConstruct.add(getEndNewString("    this.frag = frag"));
        }
        listConstruct.add("  }");
        return listConstruct;
    }
}
