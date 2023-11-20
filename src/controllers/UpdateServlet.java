package controllers;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Message;
import utils.DBUtil;

/**
 * Servlet implementation class UpdateServlet
 */
@WebServlet("/update")
public class UpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateServlet() {
        super();
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //_tokenのチェック
        String _token = request.getParameter("_token");

        if (_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();

            //更新対象のMessage取得
            Integer id = (Integer)request.getSession().getAttribute("message_id");
            Message message = em.find(Message.class, id);

            //フィールドの更新
            String title = request.getParameter("title");
            message.setTitle(title);

            String content = request.getParameter("content");
            message.setContent(content);

            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            message.setUpdated_at(currentTime);

            //データベースの更新
            em.getTransaction().begin();
            em.getTransaction().commit();

            //セッションスコープ上の不要データを削除
            request.getSession().removeAttribute("message_id");

            //indexページへリダイレクト
            response.sendRedirect(request.getContextPath() + "/index");

        }

    }

}
