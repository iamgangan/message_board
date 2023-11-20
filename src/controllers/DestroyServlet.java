package controllers;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Message;
import utils.DBUtil;

/**
 * Servlet implementation class DestroyServlet
 */
@WebServlet("/destroy")
public class DestroyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DestroyServlet() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //_tokenのチェック
        String _token = request.getParameter("_token");

        if (_token != null && _token.equals((String)request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();

            //Messageの取得
            Integer id = (Integer)request.getSession().getAttribute("message_id");
            Message message = em.find(Message.class, id);

            //DB更新（Messageデータの削除）
            em.getTransaction().begin();
            em.remove(message);
            em.getTransaction().commit();
            em.close();

            //セッションスコープから不要データの削除
            request.getSession().removeAttribute("message_id");

            //一覧画面へリダイレクト
            response.sendRedirect(request.getContextPath() + "/index");

        }

    }

}
