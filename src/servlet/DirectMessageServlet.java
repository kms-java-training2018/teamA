package servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.DirectMessageBean;
import bean.SessionBean;
import model.DirectMessageModel;

public class DirectMessageServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

		/**
		*
		*パラメータチェック画面表示処理
		*【処理概要】
		*メインメニュー画面で対象ユーザーリンクを押したときに実行される処理。
		*セッション情報チェック・会話情報取得処理が成功した場合、メッセージ画面に遷移する。
		*
		*/

		/*
		* パラメータチェック
		*/

		DirectMessageModel model = new DirectMessageModel();
		ArrayList<DirectMessageBean> list = new ArrayList<DirectMessageBean>();

		//--セッションの存在チェック--//

		// セッション情報取得
		HttpSession session = req.getSession();
		SessionBean sessionBean = new SessionBean();
		sessionBean = (SessionBean) session.getAttribute("session");

		//--パラメータチェック--//
		//main.jspから送信対象者番号取得
		//ログイン情報から送信者番号(ログイン)取得
		String toSendUserNo = req.getParameter("user_no");
		String toSendUserName = req.getParameter("user_name");
		String sendUserNo = sessionBean.getUserNo();
		String sendUserName = sessionBean.getUserName();

		DirectMessageBean bean = new DirectMessageBean();
		bean.setUserNo(sendUserNo);
		bean.setUserName(sendUserName);
		bean.setToSendUserNo(toSendUserNo);

		//--存在しなければエラー画面へ遷移--//
		//if (bean,getToSendUserNo()).equals(null)) {
		// direction = "/error";
		//}

		/*
		* 会話情報取得処理
		*/

		//--セッション情報の会員番号と、送信対象者の会員番号を条件に会話情報取得する処理--//
		try {
			list = model.getMessage(bean);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//jspに飛ばす
		req.setAttribute("messageList", list);
		req.setAttribute("toSendUserNo", toSendUserNo);
		req.setAttribute("toSendUserName", toSendUserName);

		// 取得に成功した場合セッション情報をセット
		sessionBean.setUserName(bean.getUserName());
		sessionBean.setUserNo(bean.getUserNo());
		sessionBean.setToSendUserNo(toSendUserNo);
		sessionBean.setToSendUserName(toSendUserName);
		session.setAttribute("session", sessionBean);


		//DirectMessageBean bean = new DirectMessageBean();
		//req.setAttribute("directMessage", bean);
		req.getRequestDispatcher("/WEB-INF/jsp/directMessage.jsp").forward(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

		//文字コード設定
		res.setContentType("text/html; charset=UTF-8");
		req.setCharacterEncoding("UTF-8");

		// 初期化

		DirectMessageBean bean = new DirectMessageBean();
		DirectMessageModel model = new DirectMessageModel();
		String direction = "/WEB-INF/jsp/directMessage.jsp";
		ArrayList<DirectMessageBean> list = new ArrayList<DirectMessageBean>();

		SessionBean sessionBean = new SessionBean();
		HttpSession session = req.getSession();
		sessionBean = (SessionBean) session.getAttribute("session");

		bean.setUserNo(sessionBean.getUserNo());
		bean.setToSendUserNo(sessionBean.getToSendUserNo());
		bean.setToSendUserName(sessionBean.getToSendUserName());

		//req.setAttribute("toSendUserName", req.getParameter("toSendUserName"));

		/**
		*
		*メッセージ送信処理
		*【処理概要】
		*メッセージ画面で送信ボタンを押したときに実行される処理。
		*パラメータチェックに成功した場合、会話情報テーブルにメッセージを登録する。
		*
		*/

		if (req.getParameter("send") != null) {

			/*
			 * （1）パラメータチェック
			 */

			//メッセージ画面で入力された情報を取得
			String message = req.getParameter("message");
			//--(1)-1 パラメータチェック--//
			//--(1)-2 チェックでエラーが発生した場合,エラーメッセージを設定して、//
			//メッセージ画面に遷移する。--//

			//未入力の場合
			if (message == null) {

				//エラーメッセージ設定
				bean.setErrorMessage("メッセージを入力してください");

				//桁数チェック
			} else if (message.length() > 100) {

				//エラーメッセージ設定
				bean.setErrorMessage("メッセージは100桁以内にしてください");
			}

			//--パラメータチェック完了--//
			bean.setMessage(message);

			/*
			 * (2) 会話情報登録処理
			 */

			//--(2)-1 セッション情報の会員番号を条件に、内容を登録する。--//
			try {

				//会話番号をカウントし次の番号へ
				bean = model.nextNumCheck(bean);

				//登録処理へ
				bean = model.messageRegi(bean);

				//--(2)-2 エラーメッセージがセットされていた場合はエラー画面へ--//
				if (bean.getErrorMessage() != null) {
					direction = "/error";
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		//成功した場合、そのままメッセージ画面表示

		/**
		*
		*メッセージ削除処理
		*【処理概要】
		*メッセージ画面で削除ボタンを押したときに実行される処理。
		*ダイアログで論理削除確認後、会話情報を論理削除する。
		*
		*/

		/*
		 * 確認ダイアログ表示処理
		 */

		//確認ダイアログをJSで表示する
		//OKが押下された場合以下の処理へ進む

		if (req.getParameter("delete") != null) {

			/*
			 * 会話情報論理削除処理
			 */

			//消す会話の会話番号を取得
			String deleteMessageNo = req.getParameter("messageNo");
			bean.setDeleteMessageNo(deleteMessageNo);

			//セッション情報の会員番号を条件に会話情報を論理削除する
			try {
				bean = model.DeleteMessage(bean);

				//レコードを論理削除できなかった場合
				if (bean.getErrorMessage() != null) {
					direction = "/error";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		/**
		*
		*パラメータチェック画面表示処理
		*【処理概要】
		*メインメニュー画面で対象ユーザーリンクを押したときに実行される処理。
		*セッション情報チェック・会話情報取得処理が成功した場合、メッセージ画面に遷移する。
		*
		*/

		/*
		* パラメータチェック
		*/

		//--セッションの存在チェック--//

		//--パラメータチェック--//

		//--存在しなければエラー画面へ遷移--//

		/*
		* 会話情報取得処理
		*/

		//--セッション情報の会員番号と、送信対象者の会員番号を条件に会話情報取得する処理--//
		try {
			list = model.getMessage(bean);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//jspに飛ばす
		req.setAttribute("messageList", list);
		req.setAttribute("toSendUserName", bean.getToSendUserName());

		//req.getRequestDispatcher(direction).forward(req, res);

		// 取得に成功した場合セッション情報をセット
		//        if ("".equals(bean.getErrorMessage())) {
		//            sessionBean.setUserName(bean.getUserName());
		//            sessionBean.setUserNo(bean.getUserNo());
		//            session.setAttribute("session", sessionBean);
		//        }

		req.getRequestDispatcher(direction).forward(req, res);

	}
}
