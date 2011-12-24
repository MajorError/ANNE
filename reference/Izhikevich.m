% Created by Eugene M. Izhikevich, February 25, 2003
% Excitatory neurons    Inhibitory neurons
Ne=800;                  Ni=Ne/4; 
re=rand(Ne,1);          ri=rand(Ni,1);
% Time scale of recovery variable - lower means slow recovery
a=[0.02*ones(Ne,1);     0.02+0.08*ri];
% Sensitivity of recovery to fluctuations in charge
b=[0.2*ones(Ne,1);      0.25-0.05*ri];
% Post-spike reset value
c=[-65+15*re.^2;       -65*ones(Ni,1)];
% Post-spike reset of recovery variable
d=[8-6*re.^2;           2*ones(Ni,1)];
% Synapses - inhibitory connections are 2x as strong
S=[0.5*rand(Ne+Ni,Ne), -rand(Ne+Ni,Ni)];

v=-65*ones(Ne+Ni,1);  % Initial values of v, vector of all charges
u=b.*v;               % Initial values of u; -13 for E, randomised for I
firings=[];           % spike timings

for t=1:1000          % simulation of 1000 ms 
   I=[5*randn(Ne,1);2*randn(Ni,1)]; % random thalamic input 
   fired=find(v>=30); % indices of spikes
   [fired, v(fired)]
   if ~isempty(fired)      
    % firings += pairs of (t, indices of spiking neurones)
      firings=[firings; t+0*fired, fired];
    % replace fired charges with equivalent idx from c
      v(fired)=c(fired);
      u(fired)=u(fired)+d(fired);
    % propagate firing to connected neurones
      I=I+sum(S(:,fired),2);
    end;
   v=v+0.5*(0.04*v.^2+5*v+140-u+I);
   v=v+0.5*(0.04*v.^2+5*v+140-u+I);
   u=u+a.*(b.*v-u);   
end;
plot(firings(:,1),firings(:,2),'.');