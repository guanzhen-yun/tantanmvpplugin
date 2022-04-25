package com.plugin.makefiles.factory;

import com.plugin.makefiles.bean.MvpPluginMethodBean;
import com.plugin.makefiles.constants.MvpPluginConstants;
import com.plugin.makefiles.data.MvpPluginData;
import com.plugin.makefiles.utils.MvpPluginUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * mvp生成工厂--Fragment
 */
public class MvpPluginFragFactory extends MvpPluginClassFactory {
    @Override
    String getClassName() {
        return MvpPluginData.cName;
    }

    @Override
    List<String> getImportList() {
        List<String> list = new ArrayList<>();
        list.add(getEndNewString("import android.os.Bundle"));
        if (MvpPluginData.isMvp) {
            list.add(getEndNewString("import com.p1.mobile.putong.app." + MvpPluginUtils.getParentClassName()));
        } else {
            list.add(getEndNewString("import android.view.LayoutInflater"));
            list.add(getEndNewString("import android.view.View"));
            list.add(getEndNewString("import android.view.ViewGroup"));
            list.add(getEndNewString("import androidx.annotation.Nullable"));
            list.add(getEndNewString("import com.p1.mobile.putong.app." + MvpPluginUtils.getParentClassName()));
        }
        return list;
    }

    @Override
    String getClassNameLineStr() {
        if (MvpPluginData.isMvp) {
            if (MvpPluginData.classType.equals(MvpPluginConstants.JAVA)) {
                return "public class " + MvpPluginData.cName + " extends " + MvpPluginUtils.getParentClassName() + "<" + MvpPluginUtils.getPresenterName() + ", " + MvpPluginUtils.getViewModelName() + "> {";
            } else {
                return "class " + MvpPluginData.cName + "(val bundle: Bundle) : " + MvpPluginUtils.getParentClassName() + "<" + MvpPluginUtils.getPresenterName() + ", " + MvpPluginUtils.getViewModelName() + ">() {";
            }
        } else {
            if (MvpPluginData.classType.equals(MvpPluginConstants.JAVA)) {
                return "public class " + MvpPluginData.cName + " extends " + MvpPluginUtils.getParentClassName() + " {";
            } else {
                return "class " + MvpPluginData.cName + "(val bundle: Bundle) : " + MvpPluginUtils.getParentClassName() + "() {";
            }
        }
    }

    @Override
    List<String> getArgs() {
        if (!MvpPluginData.isMvp) {
            List<String> args = new ArrayList<>();
            if (MvpPluginData.isJava()) {
                args.add(getEndNewString("  private " + MvpPluginUtils.getViewModelName() + " viewModel"));
            } else {
                args.add("  private lateinit var viewModel: " + MvpPluginUtils.getViewModelName());
            }
            return args;
        }
        return null;
    }

    @Override
    List<MvpPluginMethodBean> getMethodList() {
        List<MvpPluginMethodBean> methodBeanList = new ArrayList<>();
        //args()方法
        if (MvpPluginData.isJava()) {
            MvpPluginMethodBean argsBean = new MvpPluginMethodBean();
            argsBean.contents = getArgsContentList();
            methodBeanList.add(argsBean);
        }

        //preCreateView()方法
        MvpPluginMethodBean preCreateBean = new MvpPluginMethodBean();
        preCreateBean.contents = getPreCreateViewContentList();
        methodBeanList.add(preCreateBean);

        if (MvpPluginData.isMvp) {
            //重写initPresenter()
            MvpPluginMethodBean initPresenterBean = new MvpPluginMethodBean();
            initPresenterBean.contents = getInitPresenterContentList();
            methodBeanList.add(initPresenterBean);

            //重写initViewModel()
            MvpPluginMethodBean initViewModelBean = new MvpPluginMethodBean();
            initViewModelBean.contents = getInitViewModelContentList();
            methodBeanList.add(initViewModelBean);
        } else {
            //重写initDataOnCreate()
            MvpPluginMethodBean initDataOnCreateBean = new MvpPluginMethodBean();
            initDataOnCreateBean.contents = getInitDataOnCreateContentList();
            methodBeanList.add(initDataOnCreateBean);

            //重写inflateView()
            MvpPluginMethodBean inflateViewBean = new MvpPluginMethodBean();
            inflateViewBean.contents = getInflateViewBeanContentList();
            methodBeanList.add(inflateViewBean);
        }
        return methodBeanList;
    }

    private static List<String> getInflateViewBeanContentList() {
        List<String> listInflateView = new ArrayList<>();
        listInflateView.add("  @Nullable");
        if (MvpPluginData.isJava()) {
            listInflateView.add("  @Override");
            listInflateView.add("  protected View inflateView(LayoutInflater inflater, ViewGroup container) {");
        } else {
            listInflateView.add("  override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View {");
        }
        listInflateView.add(getEndNewString("    return viewModel.inflateView(inflater, container)"));
        listInflateView.add("  }");
        return listInflateView;
    }

    private static List<String> getInitDataOnCreateContentList() {
        List<String> listInitDataOnCreate = new ArrayList<>();
        if (MvpPluginData.isJava()) {
            listInitDataOnCreate.add("  @Override");
            listInitDataOnCreate.add("  protected void initDataOnCreate() {");
        } else {
            listInitDataOnCreate.add("  override fun initDataOnCreate() {");
        }
        listInitDataOnCreate.add(getEndNewString("    super.initDataOnCreate()"));
        if (MvpPluginData.isJava()) {
            listInitDataOnCreate.add(getEndNewString("    viewModel = new " + MvpPluginUtils.getViewModelName() + "(act(), this)"));
        } else {
            listInitDataOnCreate.add("    viewModel = " + MvpPluginUtils.getViewModelName() + "(act(), this)");
        }
        listInitDataOnCreate.add("  }");
        return listInitDataOnCreate;
    }

    private static List<String> getInitViewModelContentList() {
        List<String> listInitViewModel = new ArrayList<>();
        if (MvpPluginData.isJava()) {
            listInitViewModel.add("  @Override");
            listInitViewModel.add("  protected " + MvpPluginUtils.getViewModelName() + " initViewModel() {");
            listInitViewModel.add(getEndNewString("    return new " + MvpPluginUtils.getViewModelName() + "(act(), this)"));
            listInitViewModel.add("  }");
        } else {
            listInitViewModel.add("  override fun initViewModel() = " + MvpPluginUtils.getViewModelName() + "(act(), this)");
        }
        return listInitViewModel;
    }

    private static List<String> getInitPresenterContentList() {
        List<String> listInitPresenter = new ArrayList<>();
        if (MvpPluginData.isJava()) {
            listInitPresenter.add("  @Override");
            listInitPresenter.add("  protected " + MvpPluginUtils.getPresenterName() + " initPresenter() {");
            listInitPresenter.add(getEndNewString("    return new " + MvpPluginUtils.getPresenterName() + "(this)"));
            listInitPresenter.add("  }");
        } else {
            listInitPresenter.add("  override fun initPresenter() = " + MvpPluginUtils.getPresenterName() + "(this)");
        }
        return listInitPresenter;
    }

    private static List<String> getPreCreateViewContentList() {
        List<String> listPreCreateView = new ArrayList<>();
        if (MvpPluginData.isJava()) {
            listPreCreateView.add("  @Override");
            listPreCreateView.add("  public void preCreateView(Bundle sis) {");
        } else {
            listPreCreateView.add("  override fun preCreateView(sis: Bundle) {");
        }
        listPreCreateView.add(getEndNewString("    super.preCreateView(sis)"));
        if (MvpPluginData.isJava()) {
            listPreCreateView.add(getEndNewString("    viewModel.initArguments(getArguments())"));
        } else {
            listPreCreateView.add("    viewModel.initArguments(bundle)");
        }
        listPreCreateView.add("  }");
        return listPreCreateView;
    }

    private static List<String> getArgsContentList() {
        List<String> listArgs = new ArrayList<>();
        listArgs.add("  public static " + MvpPluginData.cName + " args() {");
        listArgs.add(getEndNewString("    " + MvpPluginData.cName + " frag = new " + MvpPluginData.cName + "()"));
        listArgs.add(getEndNewString("    Bundle bundle = new Bundle()"));
        listArgs.add(getEndNewString("    frag.setArguments(bundle)"));
        listArgs.add(getEndNewString("    return frag"));
        listArgs.add("  }");
        return listArgs;
    }
}
