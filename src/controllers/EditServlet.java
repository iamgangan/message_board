package controllers;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Message;
import utils.DBUtil;

/**
 * Servlet implementation class EditServlet
 */
@WebServlet("/edit")
public class EditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //EntityManagerの取得
        EntityManager em = DBUtil.createEntityManager();

        //パラメータの取得&DB問い合わせ
        int id = Integer.parseInt(request.getParameter("id"));
        Message message = em.find(Message.class, id);
        em.close();

        //MessageとセッションIDをリクエストスコープへセット
        request.setAttribute("message", message);
        request.setAttribute("_token", request.getSession().getId());

        //メッセージIDをセッションスコープに登録
        request.getSession().setAttribute("message_id", message.getId());

        //ページ遷移
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/messages/edit.jsp");
        rd.forward(request, response);

    }

}
