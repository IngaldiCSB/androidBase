package it.csbeng.androidbase.samples.core;

import android.content.Context;

import it.csbeng.androidbase.androidbase.core.BaseCore;

/**
 * Created by gennaro on 25/04/2016.
 */
public class LoginCore extends BaseCore<LoginCore.LoginDataDTO, Void , String>
{
    public LoginCore(BaseCore.IBaseListener listener, Context context, BaseCore<LoginDataDTO, Void, String> decorated)
    {
        super(listener, context, decorated);
    }

    @Override
    protected void execute(LoginDataDTO loginDataDTO)
    {

    }

    public interface LoginListener extends IBaseListener<Void , String>
    {}

    public class LoginDataDTO
    {
        private String username;
        private String password;

        public LoginDataDTO(String username, String password)
        {
            this.username = username;
            this.password = password;
        }

        public String getUsername()
        {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}
