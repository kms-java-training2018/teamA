package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.ProfileBean;
import bean.SessionBean;
import model.MyPageModel;

public class MyPageServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        /**
         * プロフィール情報の取得
         */

        // Beanの初期化
        SessionBean bean1 = new SessionBean();
        ProfileBean bean2 = new ProfileBean();
        bean2.setUserNo("");
        bean2.setErrorMessage("");
        bean2.setUserName("");
        bean2.setMyPageText("");

        // 初期化
        MyPageModel model = new MyPageModel();
        String direction = "/WEB-INF/jsp/myPage.jsp";

        //セッション情報の取得
        HttpSession session = req.getSession();
        bean1 = (SessionBean) session.getAttribute("session");
        bean2.setUserNo(bean1.getUserNo());

        /**
         * パラメータチェック
         *   ・セッションが存在する場合→Modelでプロフィール情報を取得
         *   ・セッションが存在しない場合→エラー画面へ
         */
        if(session != null) {
            try{
                bean2 = model.authentication(bean2);
            } catch (Exception e) {
                e.printStackTrace();
                direction = "/error";
            }
        }
        req.setAttribute("ProfileBean", bean2);
        req.getRequestDispatcher(direction).forward(req, res);
    }


    public void doPost (HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        /**
         * プロフィール情報の変更・更新
         */

        //文字コード設定
        res.setContentType("text/html; charset=UTF-8");
        req.setCharacterEncoding("UTF-8");

        // 初期化
        SessionBean bean1 = new SessionBean();
        ProfileBean bean2 = new ProfileBean();
        MyPageModel model = new MyPageModel();
        String direction = "/WEB-INF/jsp/myPage.jsp";

        // パラメータの取得
        String userNo = (String) req.getParameter("userNo");
        String userName = (String) req.getParameter("userName");
        String myPageText = (String) req.getParameter("myPageText");

        //"プロフィールを更新"のリクエスト送信後、入力値チェック
        if (userName.length() > 30 || myPageText.length() > 100) {
        	bean2.setErrorMessage("表示名は30文字以下、自己紹介文は100文字以下で入力してください。");
        } else {
            bean2.setUserName(userName);
            bean2.setUserNo(userNo);
            bean2.setMyPageText(myPageText);
            bean2.setErrorMessage("");
        }

        // 更新処理が成功した場合セッションに情報をセット
        bean1.setUserNo(userNo);
        if ("".equals(bean2.getErrorMessage())) {
            try {
                bean2 = model.authentication2(bean2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            bean1.setUserName(bean2.getUserName());
            bean1.setMyPageText(bean2.getMyPageText());
            HttpSession session = req.getSession();
            session.setAttribute("session", bean1);
            direction = "/main";
        }else {
            //更新に失敗した場合→プロフィール情報を再度DBから取得
            bean2 = model.authentication(bean2);
            req.setAttribute("ProfileBean", bean2);
        }
        req.setAttribute("session", bean1);
        req.getRequestDispatcher(direction).forward(req, res);

    }

}